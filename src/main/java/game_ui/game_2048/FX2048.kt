package game_ui.game_2048

import game_logic.game_2048.Binary2048
import game_logic.game_2048.Game2048
import game_logic.game_2048.Move
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import javafx.stage.Screen
import tornadofx.*
import kotlin.math.max
import kotlin.math.min

/**
 * Combination of the 2048 game logic and ui.
 */
class FX2048 : View(Style.TITLE) {
    private val game: Game2048 = Binary2048()

    val score: Int
        get() = game.score

    override val root = borderpane {
        addClass(Style.game2048)
        center = vbox {
            addClass(Style.root)
            alignment = Pos.TOP_CENTER
            fitToParentHeight()
            /*
            Screen.getPrimary().visualBounds.run {
                min(width, height).let {
                    primaryStage.minWidth = it * Style.WIDTH_PERCENTAGE
                    primaryStage.minHeight = it * Style.HEIGHT_PERCENTAGE
                }
            }*/

            val top = Top(Style.dpi * 0.06) {
                game.restart()
            }
            val grid = Grid(
                intArrayOf(),
                Style.dpi * 0.12
            )
            /*
            primaryStage.heightProperty().addListener {
                _, _, _ ->
                grid.prefHeight = min(height - top.height, width)
                grid.prefWidth = grid.prefHeight
                grid.maxHeight = grid.prefHeight
                grid.maxWidth = grid.prefHeight
                top.maxWidth = grid.prefHeight
                grid.minWidth = top.minWidth
                grid.minHeight = top.minHeight
            }
            primaryStage.widthProperty().addListener {
                _, _, _ ->
                grid.prefHeight = min(height - top.height, width)
                grid.prefWidth = grid.prefHeight
                grid.maxHeight = grid.prefHeight
                grid.maxWidth = grid.prefHeight
                top.maxWidth = grid.prefHeight
                grid.minWidth = top.minWidth
                grid.minHeight = top.minHeight
            }*/
            add(top)
            add(grid)
            top.maxWidthProperty().bind(grid.widthProperty())
            minWidthProperty().bind(top.minWidthProperty())
            grid.minWidthProperty().bind(top.minWidthProperty())
            grid.prefWidthProperty().bind(primaryStage.widthProperty())
            //grid.maxWidthProperty().bind(grid.heightProperty())

            grid.prefHeightProperty().bind(
                primaryStage.heightProperty().minus(
                    top.heightProperty()
                )
            )
            primaryStage.widthProperty().addListener {
                _, _, _ ->
                grid.maxWidth = min(
                    primaryStage.height - top.height,
                    primaryStage.width
                )
                grid.maxHeight = grid.maxWidth
            }
            primaryStage.heightProperty().addListener {
                _, _, _ ->
                grid.maxWidth = min(
                    primaryStage.height - top.height,
                    primaryStage.width
                )
                grid.maxHeight = grid.maxWidth
            }
            grid.minHeightProperty().bind(grid.minWidthProperty())
            minWidthProperty().addListener {
                _, _, _ ->
                primaryStage.sizeToScene()
                primaryStage.minWidth = primaryStage.width
            } // TODO: I am not sure, whether this is a good idea when I start
            //  to use the view in another view. It could influence the other
            minHeightProperty().bind(
                top.minHeightProperty().plus(grid.minHeightProperty())
            )
            minHeightProperty().addListener {
                _, _, _ -> primaryStage.minHeight = primaryStage.height
            }
        }
        setOnKeyPressed {
            when (it.code) {
                KeyCode.A -> game.play(Move.LEFT)
                KeyCode.D -> game.play(Move.RIGHT)
                KeyCode.W -> game.play(Move.UP)
                KeyCode.S -> game.play(Move.DOWN)
                else -> return@setOnKeyPressed
            }
        }
    }

    init {
        importStylesheet(Style::class)
    }

    fun play(move: Move) {
        if (move in game.possibleMoves()) {
            game.play(move)
            //top.update()
            //stopwatch.start()
        }
    }
}
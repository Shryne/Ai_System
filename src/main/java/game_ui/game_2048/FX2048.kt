package game_ui.game_2048

import game_logic.game_2048.Binary2048
import game_logic.game_2048.Game2048
import game_logic.game_2048.Move
import javafx.geometry.Pos
import javafx.scene.input.KeyCode
import javafx.stage.Screen
import tornadofx.*
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

            Screen.getPrimary().visualBounds.run {
                min(width, height).let {
                    primaryStage.minWidth = it * Style.WIDTH_PERCENTAGE
                    primaryStage.minHeight = it * Style.HEIGHT_PERCENTAGE
                }
            }
            add(Top(Style.dpi * 0.12) {
                game.restart()
            })
            //Grid()
            /*
            fun thirdAreaSize() = min(
                height - firstArea.height - secondArea.height - margin * 2.0, width - margin * 2.0)
            */
            /*
            heightProperty().onChange {
                thirdAreaSize().resizeAreas(firstArea, secondArea, thirdArea)
            }

            widthProperty().onChange {
                thirdAreaSize().resizeAreas(firstArea, secondArea, thirdArea)
            }*/
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
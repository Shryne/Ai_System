package game_ui.game_2048

import game_logic.game_2048.Board
import game_ui.game_2048.Style.Companion.fromLog
import javafx.animation.PauseTransition
import javafx.animation.ScaleTransition
import javafx.animation.TranslateTransition
import javafx.event.EventTarget
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.stage.Screen
import javafx.util.Duration
import tornadofx.*
import tornadofx.Stylesheet.Companion.button
import tornadofx.Stylesheet.Companion.label
import java.util.*
import kotlin.math.min

/**
 * This class holds the grid of 2048 and coordinates the movement of the tiles. The map size is assumed to be
 * DEFAULT_MAP_SIZE.
 */
// TODO: The maps size is still not quadratic
class Grid(private val board: Board) {
    private val gap = (Screen.getPrimary().dpi * 0.12) // TODO: CSS

    val visualContent = StackPane().apply {
        addClass(Style.grid)
        add(
            tilepane {
                prefColumns = 4
                alignment = Pos.CENTER
                hgap = gap
                vgap = gap

                val action = {
                    _: Double ->
                    prefTileWidth = sizePerTile(min(width, height))
                    prefTileHeight = prefTileWidth
                }
                widthProperty().onChange(action)
                heightProperty().onChange(action)
                (0 until board.size).forEach {
                    add(Tile(board[it]).visualContent)
                }
            }
        )
    }

    private fun sizePerTile(distributableSize: Double)
        = (distributableSize - gap * (4 + 1)) / 4 // TODO: Replace this with map.rowSize
}

class Tile(private var number: Int) {
    companion object { // debug stuff
        private const val TIME_MULTIPLIER = 1.0

        private const val SPAWN_TIME = 200.0 * TIME_MULTIPLIER
        private const val MOVE_TIME = 120.0 * TIME_MULTIPLIER
        private const val MERGE_TIME = 140.0 * TIME_MULTIPLIER
        const val FULL_MERGE_TIME = MOVE_TIME + MERGE_TIME
        const val MAX_ANIMATION_TIME = SPAWN_TIME + FULL_MERGE_TIME
    }

    var animationSpeed: Double = 1.0
    var futureNumber = number

    val visualContent = StackPane().apply {
        addClass(Style.tile, Style.tileStyles.fromLog(number))
        label {
            isVisible = (number != 0)
            text = number.toString()
            addClass(Style.tileText, Style.tileTextStyles.fromLog(number))
        }
        useMaxSize = true
    }

    fun push(to: Tile, onFinished: () -> Unit = {}) {
        futureNumber = 0
        to.futureNumber = number

        TranslateTransition(
            Duration.millis(MOVE_TIME / animationSpeed), visualContent
        ).apply {
            val oldLayoutX = to.visualContent.layoutX
            val oldLayoutY = to.visualContent.layoutY
            val oldTranslateX = to.visualContent.translateX
            val oldTranslateY = to.visualContent.translateY

            toX = oldLayoutX + oldTranslateX - visualContent.layoutX
            toY = oldLayoutY - oldTranslateY - visualContent.layoutY
            setOnFinished {
                onFinished()
                this@Tile.visualContent.layoutX = oldLayoutX
                this@Tile.visualContent.layoutY = oldLayoutY
                this@Tile.visualContent.translateX = oldTranslateX
                this@Tile.visualContent.translateY = oldTranslateY

                number = 0
                to.number = to.futureNumber
            }
        }.play()
    }

    fun merge(to: Tile) {
        push(to) {
            to.futureNumber = number * 2
            ScaleTransition(
                Duration.millis(MERGE_TIME / animationSpeed), to.visualContent
            ).apply {
                fromX = 1.3
                fromY = 1.3
                toX = 1.0
                toY = 1.0
            }.play()
        }
    }

    fun spawn() {
        PauseTransition(Duration.millis(FULL_MERGE_TIME / animationSpeed)).apply {
            setOnFinished {
                ScaleTransition(Duration.millis(
                    SPAWN_TIME / animationSpeed), visualContent
                ).apply {
                    fromX = 0.2
                    fromY = 0.2
                    toX = 1.0
                    toY = 1.0
                }.play()
                visualContent.scaleX = 0.2
                visualContent.scaleY = 0.2
                number = futureNumber
            }
        }.play()
    }
}

class Ground : StackPane() {
    /*
    var number: Int = number
        set(value) {
            visualContent.removeClass(Style.tileStyles.fromLog(field))

            field = value
            visualContent.addClass(Style.tile, Style.tileStyles.fromLog(value))
        }
*/
    init {
        addClass(Style.tile, Style.tileStyles.fromLog(0))
    }
}


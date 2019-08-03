package game_ui.game_2048

import game_ui.game_2048.Style.Companion.fromLog
import javafx.animation.PauseTransition
import javafx.animation.ScaleTransition
import javafx.animation.TranslateTransition
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.stage.Screen
import javafx.util.Duration
import tornadofx.*
import kotlin.math.min

/**
 * This class holds the grid of 2048 and coordinates the movement of the tiles. The map size is assumed to be
 * DEFAULT_MAP_SIZE.
 */
class Grid(
    private val map: IntArray,
    private val margin: Double
) : StackPane() {
    private val gap = (Screen.getPrimary().dpi * 0.12) // TODO: CSS
    private val grounds = (0 until map.size).map {
        Ground()
    }.toTypedArray()
    private val tiles = (0 until map.size).map {
        Tile(map[it])
    }.toTypedArray()

    /**
     * I use a tilePane to align the tiles and it seems to be kind of unstable. If one resizes the window, the
     * tilePane tends to get the wrong size and I am not sure why. Maybe a gridPane would work better, but because
     * it's easy to restore the correct size by resizing the window again I won't spend time on this now.
     */
    init {
        addClass(Style.grid)
        add(
            tilepane {
                alignment = Pos.CENTER
                hgap = gap
                vgap = gap

                (0..map.size).map {
                    rectangle {
                        addClass(Style.tile0)
                        widthProperty().onChange { sizePerTile(min(width, height)) }
                        heightProperty().onChange { sizePerTile(min(width, height)) }
                    }
                }
            }
        )
        add(
            tilepane {
                alignment = Pos.CENTER
                hgap = gap
                vgap = gap

                tiles.forEach { tile ->
                    add(tile)
                    //widthProperty().onChange { tile.distributeTileSize(min(width, height)) }
                    //heightProperty().onChange { tile.distributeTileSize(min(width, height)) }
                }
            }
        )
    }

    fun sizePerTile(distributableSize: Double) {
        ((distributableSize - gap * (4 + 1)) / 4) // TODO: Replace this with map.rowSize
    }
}

class Tile(number: Int) : Node() {
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

    var number: Int = number
        set(value) {
            label.removeClass(Style.tileTextStyles.fromLog(field))
            visualContent.removeClass(Style.tileStyles.fromLog(field))

            field = value
            label.text = value.toString()

            label.addClass(Style.tileText, Style.tileTextStyles.fromLog(value))
            visualContent.addClass(Style.tile, Style.tileStyles.fromLog(value))
        }

    val label = label(number.toString()) {
        addClass(Style.tileText, Style.tileTextStyles.fromLog(number))
    }

    val visualContent: Pane = run {
        stackpane {
            visibleWhen { label.textProperty().isNotEqualTo("0") }
            addClass(Style.tile, Style.tileStyles.fromLog(number))
            add(label)
        }
    }

    fun reset(newNumber: Int) {
        number = newNumber
        futureNumber = newNumber
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


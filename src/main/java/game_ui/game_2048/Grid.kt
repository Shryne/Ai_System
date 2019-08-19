package game_ui.game_2048

import game_logic.game_2048.Board
import game_ui.game_2048.tile.FXTile
import game_ui.game_2048.tile.pushRight
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.layout.TilePane
import javafx.stage.Screen
import tornadofx.*
import kotlin.math.min

/**
 * This class holds the grid of 2048 and coordinates the movement of the tiles. The map size is assumed to be
 * DEFAULT_MAP_SIZE.
 */
// TODO: The maps size is still not quadratic
class Grid(private val board: Board) {
    private val gap = (Screen.getPrimary().dpi * 0.12) // TODO: CSS
    private val tiles = FXCollections.observableArrayList(
        (0 until board.size).map { FXTile(board[it]) }
    )

    val visualContent = TilePane().apply {
        addClass(Style.grid)
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

        bindChildren(tiles) {
            it.visualContent
        }
        println(tiles)
    }

    private fun sizePerTile(distributableSize: Double)
        = (distributableSize - gap * (board.lineSize + 1)) / board.lineSize

    fun play() {
        /*
        tiles[0].push(tiles[3]) {
            tiles[0] = Tile(max(2, tiles[0].number * 2))
        }
         */
        pushRight(tiles)
    }


}
package game_ui.game_2048

import game_logic.game_2048.BinaryBoard
import game_logic.game_2048.Board
import game_logic.game_2048.Move
import game_ui.game_2048.tile.FXTile
import game_ui.game_2048.tile.makeTurn
import game_ui.game_2048.tile.pushRight
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.layout.TilePane
import javafx.stage.Screen
import tornadofx.*
import kotlin.math.abs
import kotlin.math.min

/**
 * This class holds the grid of 2048 and coordinates the movement of the tiles. The map size is assumed to be
 * DEFAULT_MAP_SIZE.
 */
// TODO: The maps size is still not quadratic
class Grid(private val board: Board) {
    private val gap = (Screen.getPrimary().dpi * 0.12) // TODO: CSS
    private val tiles = FXCollections.observableArrayList(
        (0 until board.size).map { FXTile(board[it], it) }
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
    }

    private fun sizePerTile(distributableSize: Double)
        = (distributableSize - gap * (board.lineSize + 1)) / board.lineSize

    fun singleTurnUpdate(move: Move) {
        move.forEach {
            currentIndex ->

            pushable(currentIndex, move)?.let {
                pushableIndex ->

                mergeable(pushableIndex, move)?.let {
                    tiles[it].merge(tiles[currentIndex])
                    tiles[pushableIndex].merge(tiles[currentIndex])
                } ?: tiles[pushableIndex].push(tiles[currentIndex])

            } ?: mergeable(currentIndex, move)?.let {
                tiles[it].merge(tiles[currentIndex])
            }
        }
        board.forEachIndexed { index, i ->
            if (
                i != tiles[index].futureNumber && tiles[index].futureNumber == 0
            ) {
                tiles[index].futureNumber = i
                tiles[index].spawn()
            }
        }
    }

    private val FXTile.isEmpty
        get() = futureNumber == 0

    private fun pushable(currentIndex: Int, move: Move) =
        if (tiles[currentIndex].isEmpty) tiles.find(
            currentIndex, move
        ) { !it.isEmpty }
        else null

    private fun mergeable(mergeWithIndex: Int, move: Move) =
        if (!tiles[mergeWithIndex].isEmpty)
            tiles.find(mergeWithIndex, move) {
                it.futureNumber == tiles[mergeWithIndex].futureNumber
            }
        else null

    private fun List<FXTile>.find(
        startIndex: Int, move: Move, isTarget: (FXTile) -> Boolean
    ): Int? {
        var nextIndex = nextIndex(startIndex, move)

        while (nextIndex != null) {
            if (isTarget(this[nextIndex])) return nextIndex
            else if (!this[nextIndex].isEmpty) return null
            nextIndex = nextIndex(nextIndex, move)
        }
        return null
    }

    private fun nextIndex(index: Int, move: Move): Int? {
        val nextY = index.y() + (-move.y)
        val nextX = index.x() + (-move.x)

        return if (
            nextY !in 0 until BinaryBoard.LINE_SIZE ||
            nextX !in 0 until BinaryBoard.LINE_SIZE
        ) {
            null
        } else {
            index(nextY, nextX)
        }
    }

    private fun Int.x() = this % BinaryBoard.LINE_SIZE
    private fun Int.y() = this / BinaryBoard.LINE_SIZE

    /**
     * Updates the grid by setting the numbers of the tiles directly without any animations. Use this method if the last
     * update happened more than one turn before.
     */
    fun multiTurnUpdate() {
        tiles.forEachIndexed { index, tile -> tile.reset(board[index]) }
    }

    private fun Move.forEach(action: (Int) -> Unit) {
        for (a in range()) {
            for (b in range()) {
                action(transformedIndex(dimension, a, b))
            }
        }
    }

    private fun Move.range() =
        if (isPositive) (BinaryBoard.LINE_SIZE - 1) downTo 0
        else 0 until BinaryBoard.LINE_SIZE

    private fun transformedIndex(dimension: Int, first: Int, second: Int) =
        when (dimension) {
            0 -> index(first, second)
            1 -> index(second, first)
            else -> throw UnsupportedOperationException(
                "Dimension $dimension is not supported"
            )
        }

    private fun index(y: Int, x: Int) = y * BinaryBoard.LINE_SIZE + x

    private val Move.x
        get() = when (this) {
            Move.LEFT -> -1
            Move.RIGHT -> 1
            Move.UP -> 0
            Move.DOWN -> 0
        }

    private val Move.y
        get() = when(this) {
            Move.LEFT -> 0
            Move.RIGHT -> 0
            Move.UP -> -1
            Move.DOWN -> 1
        }

    private val Move.isPositive get() = (x == 1) || (y == 1)
    private val Move.dimension get() = if (abs(x) == 1) 0 else 1
}
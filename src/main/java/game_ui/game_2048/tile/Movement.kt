/*
 * Copyright 2019 Shryne
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package game_ui.game_2048.tile

import game_logic.game_2048.BinaryBoard
import game_logic.game_2048.Board

fun <T: Tile<T>> pushLeft(tiles: List<T>) {
    for (y in 0 until BinaryBoard.LINE_SIZE) {
        for (x in 0 until BinaryBoard.LINE_SIZE) {
            val current = tiles[y, x].number
            if (current != 0) {
                for (x1 in 0 until BinaryBoard.LINE_SIZE) {
                    if (tiles[y, x1].number == 0) {
                        tiles[y, x].push(tiles[y, x1])
                    }
                }
            }
        }
    }
}


fun <T: Tile<T>> pushRight(tiles: List<T>) {
    for (y in 0 until BinaryBoard.LINE_SIZE) {
        for (x in (0 until BinaryBoard.LINE_SIZE).reversed()) {
            val current = tiles[y, x].number
            if (current != 0) {
                for (x1 in (0 until BinaryBoard.LINE_SIZE).reversed()) {
                    if (tiles[y, x1].number == 0) {
                        tiles[y, x].push(tiles[y, x1])
                    }
                }
            }
        }
    }
}

fun <T: Tile<T>> pushDown(tiles: List<T>) {
    for (y in (0 until BinaryBoard.LINE_SIZE).reversed()) {
        for (x in 0 until BinaryBoard.LINE_SIZE) {
            val current = tiles[y, x].number
            if (current != 0) {
                for (y1 in (0 until BinaryBoard.LINE_SIZE).reversed()) {
                    if (tiles[y1, x].number == 0) {
                        tiles[y, x].push(tiles[y1, x])
                    }
                }
            }
        }
    }
}

fun <T: Tile<T>> mergeLeft(tiles: List<T>) {
    for (y in 0 until BinaryBoard.LINE_SIZE) {
        for (x in 0 until BinaryBoard.LINE_SIZE) {
            val current = tiles[y, x].number
            if (current != 0) {
                for (x1 in x + 1 until BinaryBoard.LINE_SIZE) {
                    if (tiles[y, x1].number == current) {
                        tiles[y, x1].merge(tiles[y, x])
                        break
                    } else if (tiles[y, x1].number != 0) {
                        break
                    }
                }
            }
        }
    }
}

fun <T: Tile<T>> mergeRight(tiles: List<T>) {
    for (y in 0 until BinaryBoard.LINE_SIZE) {
        for (x in (0 until BinaryBoard.LINE_SIZE).reversed()) {
            val current = tiles[y, x].number
            if (current != 0) {
                for (x1 in (0 until x).reversed()) {
                    if (tiles[y, x1].number == current) {
                        tiles[y, x1].merge(tiles[y, x])
                        break
                    } else if (tiles[y, x1].number != 0) {
                        break
                    }
                }
            }
        }
    }
}

fun <T: Tile<T>> mergeDown(tiles: List<T>) {
    for (y in (0 until BinaryBoard.LINE_SIZE).reversed()) {
        for (x in 0 until BinaryBoard.LINE_SIZE) {
            val current = tiles[y, x].number
            if (current != 0) {
                for (y1 in (0 until y).reversed()) {
                    if (tiles[y1, x].number == current) {
                        tiles[y1, x].merge(tiles[y, x])
                        break
                    } else if (tiles[y1, x].number != 0) {
                        break
                    }
                }
            }
        }
    }
}

/**
 * A two dimensional getter. This method assumes that a 4x4 board is used.
 * @param row The row index. It has to be between 0 and [BinaryBoard.LINE_SIZE]
 * (exclusive).
 * @param column The column index. It has to be between 0 and
 * [BinaryBoard.LINE_SIZE] (exclusive).
 */
private operator fun <T: Tile<T>> List<T>.get(row: Int, column: Int) : T {
    require(row in 0 until BinaryBoard.LINE_SIZE) { "row: $row"}
    require(column in 0 until BinaryBoard.LINE_SIZE) { "column: $column"}
    require(size == BinaryBoard.SIZE) { "size: $size" }
    return this[row * 4 + column]
}
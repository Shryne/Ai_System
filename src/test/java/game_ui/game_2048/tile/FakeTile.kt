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

/**
 * A Tile to test the tile movements in the ui layer of 2048.
 */
class FakeTile(override var number: Int) : Tile<FakeTile> {
    override fun push(to: FakeTile, onFinished: () -> Unit) {
        to.number = number
        number = 0
    }

    override fun merge(to: FakeTile) {
        to.number = number * 2
        number = 0
    }

    override fun spawn() {

    }

    override fun equals(other: Any?) =
        when (other) {
            is FakeTile -> number == other.number
            else -> false
        }

    override fun toString() = number.toString()
    override fun hashCode() = number

}

/**
 * Uses the values to create a list of [FakeTile]s with the given values.
 * @param values The vales for the tiles. The number of given values must be
 * [BinaryBoard.SIZE].
 */
fun tileList(vararg values: Int): List<FakeTile> {
    require(values.size == BinaryBoard.SIZE) {
        "values.size: ${values.size}, BinaryBoard.SIZE: ${BinaryBoard.SIZE}"
    }
    return values.map(::FakeTile)
}
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

package game_logic.game_2048

import java.util.*

/**
 * An array for the possible moves of 2048. It assumes that it is always set
 * from start to end and reduces the amount of clearing and setting based
 * on this assumption. Clearing old values works by using [set] to overwrite
 * the old value.
 *
 * This class is mutable and not thread-safe. [set] has to be called from left
 * to right as it uses the given index + 1 as its size.
 * Note that the methods don't check for correct bounds. The maximum size of
 * this array is [Move.AMOUNT]. Trying to set on an index above this size will
 * result in an error.
 */
class PossibleMovesArray : SmallCollection<Move> {
    private val container = Array(Move.AMOUNT) { Move.LEFT } // Dummy value only
    // to initialize the array
    override var size = 0
        private set

    override operator fun get(index: Int): Move {
        return container[index]
    }

    override operator fun set(index: Int, elem: Move) {
        container[index] = elem
        size = index + 1
    }

    override fun clear() {
        size = 0
    }

    override fun toString(): String =
        Arrays.toString(Arrays.copyOfRange(container, 0, size))
}
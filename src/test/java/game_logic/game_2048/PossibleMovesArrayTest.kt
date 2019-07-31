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

import org.junit.Test
import kotlin.test.assertEquals

class PossibleMovesArrayTest {
    /**
     * [PossibleMovesArray.size] must be the last index from
     * [PossibleMovesArray.set] + 1.
     */
    @Test
    fun sizeIsBasedOnLastIndex() {
        PossibleMovesArray().apply {
            assertEquals(0, size)
            val firstSet = 3
            this[firstSet] = Move.DOWN
            assertEquals(firstSet + 1, size)
            val secondSet = 0
            this[secondSet] = Move.UP
            assertEquals(secondSet + 1, size)
        }
    }

    /**
     * [PossibleMovesArray.get] must return the correct value that was set by
     * [PossibleMovesArray.set].
     */
    @Test
    fun getAndSetElement() {
        PossibleMovesArray().apply {
            val setIndex = 2
            val setElement = Move.UP
            this[setIndex] = setElement
            assertEquals(setElement, this[setIndex])
        }
    }

    /**
     * [PossibleMovesArray.clear] must reset the Array by setting
     * [PossibleMovesArray.size] to 0.
     */
    @Test
    fun clearResetsSize() {
        PossibleMovesArray().apply {
            this[3] = Move.DOWN
            clear()
            assertEquals(0, size)
        }
    }
}
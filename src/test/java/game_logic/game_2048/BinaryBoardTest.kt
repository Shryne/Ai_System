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

import org.hamcrest.Matchers.equalTo
import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertTrue

class BinaryBoardTest {
    /**
     * [BinaryBoard]s collection constructor must prevent a construction with
     * a collection bigger than [BinaryBoard.SIZE].
     */
    @Test(expected = IllegalArgumentException::class)
    fun tooBigCollection() {
        BinaryBoard(
            List(BinaryBoard.SIZE + 1) { 0 }
        )
    }

    /**
     * [BinaryBoard]s collection constructor must prevent a construction with
     * a collection smaller than [BinaryBoard.SIZE].
     */
    @Test(expected = IllegalArgumentException::class)
    fun tooSmallCollection() {
        BinaryBoard(
            List(BinaryBoard.SIZE - 1) { 0 }
        )
    }

    /**
     * [BinaryBoard]s collection constructor must prevent a construction with
     * a collection with fields smaller than 0.
     */
    @Test(expected = IllegalArgumentException::class)
    fun tooSmallValueInCollection() {
        BinaryBoard(
            listOf(
                0, 0, 1, 6,
                2, 6, 2, 6,
                -1, 7, 2, 8,
                4, 7, 4, 10
            )
        )
    }

    /**
     * [BinaryBoard]s collection constructor must prevent a construction with
     * a collection with fields greater than [BinaryBoard.LARGEST_ELEMENT].
     */
    @Test(expected = IllegalArgumentException::class)
    fun tooBigValueInCollection() {
        BinaryBoard(
            listOf(
                0, 9, 1, 6,
                2, 6, 12, 6,
                2, 7, 2, 16,
                4, 7, 4, 10
            )
        )
    }

    /**
     * [BinaryBoard]s collection construction with a board of zeros must convert
     * it to a 0L board.
     */
    @Test
    fun zeroIntArrayConstruction() {
        assertThat(
            BinaryBoard(List(BinaryBoard.SIZE) { 0 })[0],
            equalTo(0)
        )
    }

    /**
     * [BinaryBoard]s constructed with:
     * ```
     * [val, 0, 0, 0]
     * [0, 0, 0, 0]
     * [0, 0, 0, 0]
     * [0, 0, 0, 0]
     * ```
     * must convert it to a board where the first field has the value.
     */
    @Test
    fun firstFieldIntArrayConstruction() {
        val value = 14
        assertEquals(
            BinaryBoard(
                listOf(
                    value, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                )
            )[0],
            value
        )
    }

    /**
     * [BinaryBoard]s constructed with:
     * ```
     * [0, 0, 0, 0]
     * [0, 0, 0, 0]
     * [0, 0, 0, 0]
     * [0, 0, 0, val]
     * ```
     * must convert it to board where the last field has the value.
     */
    @Test
    fun lastFieldIntArrayConstruction() {
        val value = 8
        assertEquals(
            BinaryBoard(
                listOf(
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, value
                )
            )[BinaryBoard.SIZE - 1],
            value
        )
    }

    /**
     * [BinaryBoard.get] must return the first element when it's called with 0.
     */
    @Test
    fun getFirstElement() {
        val value = 1L
        assertEquals(
            value.toInt(),
            BinaryBoard(
                value.shl(Long.SIZE_BITS - BinaryBoard.BITS_PER_TILE)
            )[0]
        )
    }

    /**
     * [BinaryBoard.get] must return the last element when it's called with
     * [BinaryBoard.SIZE].
     */
    @Test
    fun getLastElement() {
        val value = 13L
        assertEquals(
            value.toInt(),
            BinaryBoard(value)[15]
        )
    }

    /**
     * [BinaryBoard.set] must set the first element when it's called with 0.
     */
    @Test
    fun setFirstElement() {
        val value = 4L
        BinaryBoard().apply {
            val index = 0
            this[index] = value
            assertEquals(value.toInt(), this[index])
        }
    }

    /**
     * [BinaryBoard.set] must set the last element when it's called with
     * [BinaryBoard.SIZE].
     */
    @Test
    fun setLastElement() {
        val value = 8L
        BinaryBoard().apply {
            val index = BinaryBoard.SIZE - 1
            this[index] = value
            assertEquals(value.toInt(), this[index])
        }
    }

    /**
     * The iterator from [BinaryBoard.iterator] must return the first element
     * when [Iterator.next] is called.
     */
    @Test
    fun iteratesFirstElement() {
        val value = 1L
        assertEquals(
            value.toInt(),
            BinaryBoard(
                value.shl(Long.SIZE_BITS - BinaryBoard.BITS_PER_TILE)
            ).iterator().next()
        )
    }

    /**
     * The iterator from [BinaryBoard.iterator] must return all fields in the
     * correct order when [Iterator.next] is called.
     */
    @Test
    fun iteratesOverAllFields() {
        val values = listOf(
            2, 3, 4, 5,
            2, 3, 1, 12,
            3, 9, 10, 5,
            12, 6, 3, 7
        )
        assertEquals(
            values,
            BinaryBoard(values).iterator().asSequence().toList()
        )
    }

    /**
     * The iterator from [BinaryBoard.iterator] must return true on
     * [Iterator.hasNext] when it's called on the first element.
     */
    @Test
    fun hasNextOnFirstElement() {
        assertTrue(BinaryBoard(6).iterator().hasNext())
    }

    /**
     * The iterator from [BinaryBoard.iterator] must return true on
     * [Iterator.hasNext] when it's cursor is on the last element.
     */
    @Test
    fun hasNextOnLastElement() {
        val values = List(BinaryBoard.SIZE) { 0 }
        BinaryBoard(values).iterator().apply {
            repeat(values.size - 1) { next() }
            assertTrue(hasNext())
        }
    }

    /**
     * The iterator from [BinaryBoard.iterator] must return false on
     * [Iterator.hasNext] when it's cursor is after the last element.
     */
    @Test
    fun hasNextAfterLastElement() {
        val values = List(BinaryBoard.SIZE) { 0 }
        BinaryBoard(values).iterator().apply {
            repeat(values.size) { next() }
            assertFalse(hasNext())
        }
    }

    /**
     * The iterator from [BinaryBoard.iterator] must throw a
     * [NoSuchElementException] when it's called after there isn't any element
     * left.
     */
    @Test(expected = NoSuchElementException::class)
    fun nextAfterLastElement() {
        val values = List(BinaryBoard.SIZE) { 0 }
        BinaryBoard(values).iterator().apply {
            repeat(values.size + 1) { next() }
        }
    }
}
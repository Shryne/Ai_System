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

package game_ui.game_2048

import game_ui.game_2048.tile.assert
import game_ui.game_2048.tile.mergeLeft
import game_ui.game_2048.tile.pushLeft
import org.junit.Test

/**
 * Tests for [pushLeft] and [mergeLeft]
 */
class MovementLeftTest {
    /**
     * [pushLeft] with one pushable tile must push it.
     */
    @Test
    fun pushLeftPushOne() {
        assert(
            intArrayOf(
                0, 0, 0, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushLeft
        )
    }

    /**
     * [pushLeft] with two pushable tiles must push them.
     */
    @Test
    fun pushLeftPushTwo() {
        assert(
            intArrayOf(
                0, 0, 2, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                2, 1, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushLeft
        )
    }

    /**
     * [pushLeft] with tiles in different rows must push them all.
     */
    @Test
    fun pushLeftPushRows() {
        assert(
            intArrayOf(
                0, 0, 0, 1,
                0, 1, 0, 0,
                0, 0, 0, 0,
                0, 0, 1, 0
            ),
            intArrayOf(
                1, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0
            ),
            ::pushLeft
        )
    }

    /**
     * [pushLeft] with no pushable tiles must be a no-op.
     */
    @Test
    fun pushLeftNoOp() {
        assert(
            intArrayOf(
                1, 2, 3, 4,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                1, 2, 3, 4,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushLeft
        )
    }

    /**
     * [mergeLeft] with two tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeLeftTwoTiles() {
        assert(
            intArrayOf(
                0, 1, 1, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 2, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeLeft
        )
    }

    /**
     * [mergeLeft] with two tiles with a gap that can be merged must merge
     * these tiles.
     */
    @Test
    fun mergeLeftWithGap() {
        assert(
            intArrayOf(
                0, 1, 0, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 2, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeLeft
        )
    }

    /**
     * [mergeLeft] with four tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeLeftFour() {
        assert(
            intArrayOf(
                1, 1, 1, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 2, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeLeft
        )
    }

    /**
     * [mergeLeft] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeLeftThree() {
        assert(
            intArrayOf(
                1, 1, 1, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 1, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeLeft
        )
    }

    /**
     * [mergeLeft] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeLeftThreeTilesWithBorder() {
        assert(
            intArrayOf(
                1, 2, 1, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                1, 2, 1, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeLeft
        )
    }
}
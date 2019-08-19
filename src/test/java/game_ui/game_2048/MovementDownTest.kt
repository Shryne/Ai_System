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

import game_ui.game_2048.tile.*
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [pushDown] and [mergeDown]
 */
class MovementDownTest {
    /**
     * [pushDown] with one pushable tile must push it.
     */
    @Test
    fun pushDownPushOne() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0
            ),
            ::pushDown
        )
    }

    /**
     * [pushDown] with two pushable tiles must push them.
     */
    @Test
    fun pushDownPushTwo() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0,
                2, 0, 0, 0
            ),
            ::pushDown
        )
    }

    /**
     * [pushDown] with tiles in different rows must push them all.
     */
    @Test
    fun pushDownPushRows() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 0, 1,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                1, 1, 0, 1
            ),
            ::pushDown
        )
    }

    /**
     * [pushDown] with no pushable tiles must be a no-op.
     */
    @Test
    fun pushDownNoOp() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                2, 0, 0, 0,
                3, 0, 0, 0,
                4, 0, 0, 0
            ),
            intArrayOf(
                1, 0, 0, 0,
                2, 0, 0, 0,
                3, 0, 0, 0,
                4, 0, 0, 0
            ),
            ::pushDown
        )
    }

    /**
     * [mergeDown] with two tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeDownTwoTiles() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeDown
        )
    }

    /**
     * [mergeDown] with two tiles with a gap that can be merged must merge
     * these tiles.
     */
    @Test
    fun mergeDownWithGap() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                0, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeDown
        )
    }

    /**
     * [mergeDown] with four tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeDownFour() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0,
                2, 0, 0, 0
            ),
            ::mergeDown
        )
    }

    /**
     * [mergeDown] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeDownThree() {
        assert(
            intArrayOf(
                0, 0, 0, 0,
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                2, 0, 0, 0
            ),
            ::mergeDown
        )
    }

    /**
     * [mergeDown] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeDownThreeTilesWithBorder() {
        assert(
            intArrayOf(
                0, 0, 0, 0,
                1, 0, 0, 0,
                2, 0, 0, 0,
                1, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                1, 0, 0, 0,
                2, 0, 0, 0,
                1, 0, 0, 0
            ),
            ::mergeDown
        )
    }
}
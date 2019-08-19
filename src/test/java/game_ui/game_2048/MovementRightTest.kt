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
import game_ui.game_2048.tile.mergeRight
import game_ui.game_2048.tile.pushRight
import game_ui.game_2048.tile.tileList
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [pushRight] and [mergeRight]
 */
class MovementRightTest {
    /**
     * [pushRight] with one pushable tile must push it.
     */
    @Test
    fun pushRightPushOne() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushRight
        )
    }

    /**
     * [pushRight] with two pushable tiles must push them.
     */
    @Test
    fun pushRightPushTwo() {
        assert(
            intArrayOf(
                1, 2, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 1, 2,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushRight
        )
    }

    /**
     * [pushRight] with tiles in different rows must push them all.
     */
    @Test
    fun pushRightPushRows() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 0, 0,
                0, 0, 1, 0
            ),
            intArrayOf(
                0, 0, 0, 1,
                0, 0, 0, 1,
                0, 0, 0, 0,
                0, 0, 0, 1
            ),
            ::pushRight
        )
    }

    /**
     * [pushRight] with no pushable tiles must be a no-op.
     */
    @Test
    fun pushRightNoOp() {
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
            ::pushRight
        )
    }

    /**
     * [mergeRight] with two tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeRightTwoTiles() {
        assert(
            intArrayOf(
                1, 1, 0, 0,
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
            ::mergeRight
        )
    }

    /**
     * [mergeRight] with two tiles with a gap that can be merged must merge
     * these tiles.
     */
    @Test
    fun mergeRightWithGap() {
        assert(
            intArrayOf(
                1, 0, 1, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 2, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeRight
        )
    }

    /**
     * [mergeRight] with four tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeRightFour() {
        assert(
            intArrayOf(
                1, 1, 1, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 2, 0, 2,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeRight
        )
    }

    /**
     * [mergeRight] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeRightThree() {
        assert(
            intArrayOf(
                0, 1, 1, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 1, 0, 2,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeRight
        )
    }

    /**
     * [mergeRight] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeRightThreeTilesWithBorder() {
        assert(
            intArrayOf(
                0, 1, 2, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 1, 2, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeRight
        )
    }
}
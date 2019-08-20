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

import game_logic.game_2048.Move
import game_ui.game_2048.tile.*
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [makeTurn], [pushUp] and [mergeUp].
 */
class MovementUpTest {
    /**
     * [makeTurn] on a line with a pushable and mergeable tile must make the
     * right turn.
     */
    @Test
    fun makeTurnOnPushMergeableBoard() {
        assert(
            intArrayOf(
                2, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            )
        ) {
            makeTurn(Move.UP, it)
        }
    }

    /**
     * [pushUp] with one pushable tile must push it.
     */
    @Test
    fun pushUpOne() {
        assert(
            intArrayOf(
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0
            ),
            intArrayOf(
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushUp
        )
    }

    /**
     * [pushUp] with two mergeable tiles must keep them as they were.
     */
    @Test
    fun pushTwoMergeables() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                1, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushUp
        )
    }

    /**
     * [pushUp] with two pushable tiles must push them.
     */
    @Test
    fun pushUpTwo() {
        assert(
            intArrayOf(
                0, 0, 0, 0,
                0, 0, 0, 0,
                2, 0, 0, 0,
                1, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushUp
        )
    }

    /**
     * [pushUp] with tiles in different rows must push them all.
     */
    @Test
    fun pushUpRows() {
        assert(
            intArrayOf(
                0, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 0, 1,
                1, 0, 0, 0
            ),
            intArrayOf(
                1, 1, 0, 1,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::pushUp
        )
    }

    /**
     * [pushUp] with no pushable tiles must be a no-op.
     */
    @Test
    fun pushUpNoOp() {
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
            ::pushUp
        )
    }

    /**
     * [mergeUp] with two tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeUpTwoTiles() {
        assert(
            intArrayOf(
                0, 0, 0, 0,
                1, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                0, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeUp
        )
    }

    /**
     * [mergeUp] with two tiles with a gap that can be merged must merge
     * these tiles.
     */
    @Test
    fun mergeUpWithGap() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeUp
        )
    }

    /**
     * [mergeUp] with four tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeUpFour() {
        assert(
            intArrayOf(
                2, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 0, 0,
                0, 0, 0, 0,
                2, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeUp
        )
    }

    /**
     * [mergeUp] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeUpThree() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                1, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                2, 0, 0, 0,
                0, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeUp
        )
    }

    /**
     * [mergeUp] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeUpThreeTilesWithBorder() {
        assert(
            intArrayOf(
                1, 0, 0, 0,
                2, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            intArrayOf(
                1, 0, 0, 0,
                2, 0, 0, 0,
                1, 0, 0, 0,
                0, 0, 0, 0
            ),
            ::mergeUp
        )
    }
}
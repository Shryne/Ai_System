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

import game_ui.game_2048.tile.mergeRight
import game_ui.game_2048.tile.pushRight
import game_ui.game_2048.tile.tileList
import junit.framework.Assert.assertEquals
import org.junit.Test

class MovementTest {
    /**
     * [pushRight] with one pushable tile must push it.
     */
    @Test
    fun moveRightPushOne() {
        tileList(
            1, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            pushRight(this)
            assertEquals(
                tileList(
                    0, 0, 0, 1,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [pushRight] with two pushable tiles must push them.
     */
    @Test
    fun moveRightPushTwo() {
        tileList(
            1, 2, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            pushRight(this)
            assertEquals(
                tileList(
                    0, 0, 1, 2,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [pushRight] with tiles in different rows must push them all.
     */
    @Test
    fun moveRightPushRows() {
        tileList(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 0, 0,
            0, 0, 1, 0
        ).apply {
            pushRight(this)
            assertEquals(
                tileList(
                    0, 0, 0, 1,
                    0, 0, 0, 1,
                    0, 0, 0, 0,
                    0, 0, 0, 1
                ),
                this
            )
        }
    }

    /**
     * [pushRight] with no pushable tiles must be a no-op.
     */
    @Test
    fun moveRightNoOp() {
        tileList(
            1, 2, 3, 4,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            pushRight(this)
            assertEquals(
                tileList(
                    1, 2, 3, 4,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [mergeRight] with two tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeRightTwoTiles() {
        tileList(
            1, 1, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            mergeRight(this)
            assertEquals(
                tileList(
                    0, 2, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [mergeRight] with two tiles with a gap that can be merged must merge
     * these tiles.
     */
    @Test
    fun mergeRightWithGap() {
        tileList(
            1, 0, 1, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            mergeRight(this)
            assertEquals(
                tileList(
                    0, 0, 2, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [mergeRight] with four tiles that can be merged must merge these tiles.
     */
    @Test
    fun mergeRightFour() {
        tileList(
            1, 1, 1, 1,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            mergeRight(this)
            assertEquals(
                tileList(
                    0, 2, 0, 2,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [mergeRight] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeRightThree() {
        tileList(
            0, 1, 1, 1,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            mergeRight(this)
            assertEquals(
                tileList(
                    0, 1, 0, 2,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }

    /**
     * [mergeRight] with three tiles that can be merged must merge the correct
     * two tiles.
     */
    @Test
    fun mergeRightThreeTilesWithBorder() {
        tileList(
            0, 1, 2, 1,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0
        ).apply {
            mergeRight(this)
            assertEquals(
                tileList(
                    0, 1, 2, 1,
                    0, 0, 0, 0,
                    0, 0, 0, 0,
                    0, 0, 0, 0
                ),
                this
            )
        }
    }
}
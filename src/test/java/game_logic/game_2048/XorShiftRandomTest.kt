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

import game_logic.game_2048.random.XorShiftRandom
import org.junit.Test
import java.util.*
import kotlin.test.assertTrue

class XorShiftRandomTest {
    /**
     * [XorShiftRandom.next] must only produce values above that are zero or
     * greater.
     */
    @Test
    fun notNegative() =
        XorShiftRandom().run {
            for (i in 0..1_000) {
                assertTrue { next(12) >= 0 }
            }
        }

    /**
     * [XorShiftRandom.next] must only produce values below the given max
     * argument.
     */
    @Test
    fun belowMax() {
        val max = 15
        XorShiftRandom().run {
            for (i in 0..1_000) {
                assertTrue { next(max) < max }
            }
        }
    }

    /**
     * [XorShiftRandom.next] must produce all numbers with the same probability.
     */
    @Test
    fun distributedNumbers() {
        val max = 10
        val counts = IntArray(max)
        val runs = 100_000
        val error = 0.05
        XorShiftRandom().run {
            for (i in 0..runs) {
                ++counts[next(max)]
            }
        }
        val expected = runs / max
        val minExpected = expected - expected * error
        val maxExpected = expected + expected * error
        counts.forEach {
            i -> assertTrue(
            minExpected < i && i < maxExpected,
            "Expected i to be between $minExpected and $maxExpected " +
                "but is: $i. All values: ${Arrays.toString(counts)}"
            )
        }
    }
}
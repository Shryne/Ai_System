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

package game_logic.game_2048.random

/**
 * Uses the xor shift algorithm by George Marsaglia to generate a random number.
 * [next] should be used for this purpose. The method mutates the state of the
 * object.
 *
 * Instances of this class are mutable and not thread-safe.
 */
class XorShiftRandom(
    private var seed: Long = System.currentTimeMillis()
) : Random {

    /**
     * This method mutates the state of the object.
     * @return A positive number between 0 (inclusive) and max (exclusive).
     */
    override fun next(max: Int): Int = Math.floorMod(next().toInt(), max)

    /**
     * This method mutates the state of the object.
     * @return A number between [Long.MIN_VALUE] and [Long.MAX_VALUE] (I guess).
     */
    private fun next(): Long {
        seed = seed xor (seed shl 21)
        seed = seed xor seed.ushr(35)
        seed = seed xor (seed shl 4)
        return seed
    }
}
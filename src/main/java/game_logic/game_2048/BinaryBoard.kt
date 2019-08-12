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

/**
 * The map of the binary 2048 version ([Binary2048]). It assumes that the game
 * has a size of 4*4 fields. This binary offers some additional methods necessary
 * for the binary game.
 * Instances of this class are mutable and not thread-safe.
 * @param binary The binary representation of the game. 4 bits of the long
 * represent one field that can have a value from 0-15 (32k in the game).
 * TODO: Check whether adding the shift operations decreases the performance
 */
class BinaryBoard(var binary: Long = 0) : Board {
    companion object {
        const val BITS_PER_TILE = 4
    }

    override val rowSize = 4
    override val size = rowSize * 4

    private val random = XorShiftRandom()

    override operator fun get(index: Int): Int =
        (binary shr BITS_PER_TILE * (size - index - 1)
            shl java.lang.Long.SIZE - BITS_PER_TILE)
            .ushr(java.lang.Long.SIZE - BITS_PER_TILE).toInt()

    operator fun set(index: Int, value: Long) {
        binary = binary xor (value shl (size - index - 1) * BITS_PER_TILE)
    }

    operator fun contains(value: Int): Boolean {
        for (i in 0 until size) {
            if (this[i] == value) return true
        }
        return false
    }

    fun insertRandomTile(number: Int, amountOfEmpties: Int) {
        val emptySpawnIndex = random.next(amountOfEmpties)
        var emptiesCounter = -1
        var spawnIndex = -1

        while (emptiesCounter < emptySpawnIndex) {
            spawnIndex++
            if (this[spawnIndex] == 0) emptiesCounter++
        }
        this[spawnIndex] = number.toLong()
    }
}
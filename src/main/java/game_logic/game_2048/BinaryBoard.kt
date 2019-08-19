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
 * represent one field that can have a value from 0-15 (32k in the game). A
 * value of 1 translates to:
 * ```
 * [0, 0, 0, 0]
 * [0, 0, 0, 0]
 * [0, 0, 0, 0]
 * [0, 0, 0, 1]
 * ```
 * And a value of 0b0001_0000_(54 more 0s) is:
 * ```
 * [1, 0, 0, 0]
 * [0, 0, 0, 0]
 * [0, 0, 0, 0]
 * [0, 0, 0, 0]
 * ```
 */
// TODO: Check whether adding the shift operations decreases the performance
class BinaryBoard(var binary: Long = 0) : Board {
    /**
     * Convenience constructor for debugging and testing purposes. It isn't
     * intended to be used in production code.
     * @param collection The collection to create the board from. The size of
     * the collection must be [SIZE] and all elements have to be
     * between 0 and [LARGEST_ELEMENT].
     */
    constructor(collection: Collection<Int>) : this(
        collection.foldIndexed(0L) {
            i, accu, elem ->
            accu or elem.toLong().shl(
                Long.SIZE_BITS - BITS_PER_TILE * (i + 1)
            )
        }.apply {
            require(collection.size == SIZE) {
                "size: ${collection.size}"
            }
            require(collection.all { it in 0..LARGEST_ELEMENT }) {
                "collection: $collection"
            }
        }
    )

    /**
     * Convenience constructor for debugging and testing purposes. It isn't
     * intended to be used in production code.
     * @param array The array to create the board from. The size of
     * the array must be [SIZE] and all elements have to be
     * between 0 and [LARGEST_ELEMENT].
     */
    constructor(array: IntArray) : this(array.toList())

    companion object {
        /**
         * The default/currently supported size of the rows and the columns.
         */
        const val LINE_SIZE = 4

        /**
         * The default/currently supported size of the board.
         */
        const val SIZE = LINE_SIZE * LINE_SIZE

        /**
         * The number of bits per field. A 4 means, that 4 bits are taken for
         * each field, which translates to values from 0 to 15. Since 2048
         * uses only quadratic values, the values are converted to 0 to ~64k.
         */
        const val BITS_PER_TILE = 4

        /**
         * The largest possible number of a field.
         */
        const val LARGEST_ELEMENT = Long.SIZE_BITS / BITS_PER_TILE - 1
    }

    override val lineSize = 4
    override val size = SIZE

    /**
     * The generator to spawn numbers each turn at random positions.
     */
    // TODO: This should be injected to enable tests.
    private val random = XorShiftRandom()

    override operator fun get(index: Int): Int {
        assert(index in 0 until size) {
            "index: $index, size: $size"
        }
        return (binary shr BITS_PER_TILE * (size - index - 1)
            shl java.lang.Long.SIZE - BITS_PER_TILE)
            .ushr(java.lang.Long.SIZE - BITS_PER_TILE).toInt()
    }

    override fun get(row: Int, column: Int): Int {
        assert(row in 0 until lineSize) {
            "row: $row, lineSize: $lineSize"
        }
        assert(column in 0 until lineSize) {
            "column: $column, lineSize: $lineSize"
        }
        return this[row * lineSize + column]
    }

    operator fun set(index: Int, value: Long) {
        assert(index in 0 until size) {
            "index: $index, size: $size"
        }
        binary = binary xor (value shl (size - index - 1) * BITS_PER_TILE)
    }

    operator fun contains(value: Int): Boolean {
        for (i in 0 until size) {
            if (this[i] == value) return true
        }
        return false
    }

    /**
     * Inserts a random tile into the board. This should be called after a turn
     * has been made or twice at the beginning of the game.
     * @param number The number to be spawned. Normally, it's 1 or 2. This
     * numbers has to be between 0 and 15. For performance reasons the check is
     * done by [assert].
     * @param amountOfEmpties The amount of empty fields. It's necessary to
     * calculate the empty field to be used to spawn the value. The number must
     * be between 1 and [BinaryBoard.size]. For performance reasons the check is
     * done by [assert].
     */
    fun insertRandomTile(number: Int, amountOfEmpties: Int) {
        assert(number in 0 until size) {
            "number: $number, size: $size"
        }
        assert(amountOfEmpties in 1 until size) {
            "amountOfEmpties: $amountOfEmpties, size: $size"
        }
        val emptySpawnIndex = random.next(amountOfEmpties)
        var emptiesCounter = -1
        var spawnIndex = -1

        while (emptiesCounter < emptySpawnIndex) {
            spawnIndex++
            if (this[spawnIndex] == 0) emptiesCounter++
        }
        this[spawnIndex] = number.toLong()
    }

    override fun iterator() =
        object : Iterator<Int> {
            private var cursor = -1

            override fun hasNext() = cursor + 1 < size
            override fun next() =
                if (hasNext()) {
                    ++cursor
                    get(cursor)
                } else {
                    throw NoSuchElementException(
                        "The board has only $size elements, but the cursor"
                            + "is: $cursor"
                    )
                }
        }

    override fun toString(): String {
        val result = StringBuilder("[")
        for (i in 0 until size) {
            result.append(this[i])
            if ((i + 1) % size == 0) {
                result.append("]\n[")
            } else {
                result.append(", ")
            }
        }
        return result.deleteCharAt(result.length - 1)
            .deleteCharAt(result.length - 1).toString()
    }
}
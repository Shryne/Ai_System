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

/**
 * A quadratic board of the 2048 game.
 */
interface Board : Iterable<Int> {
    /**
     * The number of field on the map (normally 16).
     */
    val size: Int

    /**
     * The number of fields in a line (normally 4).
     */
    val lineSize: Int

    /**
     * Returns the field on the given index.
     * @param index The index of the field. It has to be between 0 and [size].
     * @return The value of the field on the index.
     */
    operator fun get(index: Int): Int

    /**
     * Returns the field on the given index.
     * @param row The index of the row. It has to be between 0 and [lineSize].
     * @param column THe index of the column. It has to be between 0 and
     *  [lineSize].
     * @return The value of the field on the index.
     */
    operator fun get(row: Int, column: Int): Int
}
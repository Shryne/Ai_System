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
 * The 2048 game.
 */
interface Game2048 {
    /**
     * The current score of the game. It's the sum of all numbers that were
     * created by a merge.
     */
    val score: Int

    /**
     * The highest tile of the board.
     */
    val highestTile: Int

    /**
     * Whether the game is over or not.
     */
    val isOver: Boolean

    /**
     * Plays the given move. This method mutates the game.
     */
    fun play(move: Move)

    /**
     * Returns the moves that are playable for the current board.
     */
    fun possibleMoves(): SmallCollection<Move>

    /**
     * Restarts the game.
     */
    fun restart()
}
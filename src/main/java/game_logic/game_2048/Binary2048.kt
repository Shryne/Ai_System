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
import kotlin.math.max

/**
 * A highly optimized implementation of the 2048 game base on @nneonneo's
 * implementation in c++ from [https://github.com/nneonneo/2048-ai]. It creates
 * statically all possible states (up to 32k) to uses them later for look up.
 *
 * Instances of this class are mutable and not thread-safe.
 */
// TODO: Adjust lines to fit in the 80 characters border
class Binary2048(override val board: BinaryBoard) : Game2048 {
    constructor(binary: Long = 0) : this(BinaryBoard(binary))

    /**
     * The score value of the binary before any merge happened. This is
     * necessary, because [SCORE_TABLE] assumes that the values are the result
     * of merges - even for a 4 that spawned at the beginning.
     */
    private var initialScore = 0

    /**
     * The moves that are available for the current state.
     */
    private val possibleMoves = PossibleMovesArray()

    /**
     * A helper variable to reduce the amount of recalculations of the
     * possible moves.
     */
    private var possibleMovesDetermined = false // for the current turn

    override val isOver: Boolean
        get() = executeMoveUp(board.binary) == board.binary
            && executeMoveLeft(board.binary) == board.binary
            && executeMoveRight(board.binary) == board.binary
            && executeMoveDown(board.binary) == board.binary

    override val score: Int
        get() = scoreHelper(board.binary) - initialScore

    override val highestTile: Int
        get() {
            var highestTile = 0
            for (i in 0 until DEFAULT_MAP_SIZE) {
                highestTile = max(highestTile, board[i])
            }
            return pow(2, highestTile)
        }

    init {
        restart()
    }

    override fun play(move: Move) {
        val backup = board.binary
        board.binary = when (move) {
            Move.LEFT -> executeMoveLeft(backup)
            Move.RIGHT -> executeMoveRight(backup)
            Move.UP -> executeMoveUp(backup)
            Move.DOWN -> executeMoveDown(backup)
        }
        if (board.binary != backup) {

            board.insertRandomTile(randomNumber(), countEmpty(board.binary))
            possibleMovesDetermined = false
        }
    }

    override fun restart() {
        board.insertRandomTile(randomNumber(), board.size)
        board.insertRandomTile(randomNumber(),board.size - 1)
        initialScore = score
        determinePossibleMoves()
    }

    override fun possibleMoves(): SmallCollection<Move> {
        if (!possibleMovesDetermined) determinePossibleMoves()
        return possibleMoves
    }

    override fun toString(): String {
        val result = StringBuilder("[score: ").append(score).append("]\n[")
        for (i in 0 until DEFAULT_MAP_SIZE) {
            result.append(board[i])

            if ((i + 1) % DEFAULT_MAP_ROW_LENGTH == 0) {
                result.append("]\n[")
            } else {
                result.append(", ")
            }
        }
        return result.deleteCharAt(result.length - 1)
            .deleteCharAt(result.length - 1).toString()
    }

    private fun countEmpty(board: Long): Int {
        var board = board
        board = board or (board shr 2 and 0x3333333333333333L)
        board = board or (board shr 1)
        board = board.inv() and 0x1111111111111111L
        board += board shr 32
        board += board shr 16
        board += board shr 8
        board += board shr 4
        return (board and 0xf).toInt()
    }

    private fun determinePossibleMoves() {
        var possibleMovesAmount = 0
        if (executeMoveLeft(board.binary) != board.binary) {
            possibleMoves[possibleMovesAmount] = Move.LEFT
            possibleMovesAmount++
        }

        if (executeMoveRight(board.binary) != board.binary) {
            possibleMoves[possibleMovesAmount] = Move.RIGHT
            possibleMovesAmount++
        }

        if (executeMoveUp(board.binary) != board.binary) {
            possibleMoves[possibleMovesAmount] = Move.UP
            possibleMovesAmount++
        }

        if (executeMoveDown(board.binary) != board.binary) {
            possibleMoves[possibleMovesAmount] = Move.DOWN
        }
        possibleMovesDetermined = true
    }

    /**
     * source: [http://stackoverflow.com/a/10517609/7563350]
     */
    private fun pow(base: Int, exp: Int): Int {
        var base = base
        var exp = exp
        var result = 1
        while (exp != 0) {
            if (exp and 1 == 1) result *= base
            exp = exp shr 1
            base *= base
        }

        return result
    }

    private fun randomNumber(): Int {
        return if (random.next(10) < 9) 1 else 2
    }

    companion object {
        private const val DEFAULT_MAP_ROW_LENGTH = 4
        private const val DEFAULT_MAP_SIZE = DEFAULT_MAP_ROW_LENGTH * DEFAULT_MAP_ROW_LENGTH

        private const val ROW_MASK = 0xFFFFL
        private const val COL_MASK = 0x000F000F000F000FL

        private val ROW_LEFT_TABLE = LongArray(65536)
        private val ROW_RIGHT_TABLE = LongArray(65536)
        private val COL_UP_TABLE = LongArray(65536)
        private val COL_DOWN_TABLE = LongArray(65536)
        private val SCORE_TABLE = IntArray(65536)

        private val random = XorShiftRandom()

        init {
            for (row in 0..65535) {
                // create all possible lines, from 0000 to 15-15-15-15
                // (32k - 32k - 32k - 32k)
                val line = longArrayOf(
                    row.toLong() and 0xFL,
                    row.toLong() shr 4 and 0xFL,
                    row.toLong() shr 8 and 0xFL,
                    row.toLong() shr 12 and 0xFL
                )

                var score = 0
                for (i in 0..3) {
                    val rank = line[i]
                    if (rank >= 2) {
                        score += ((rank - 1) * (1 shl rank.toInt())).toInt()
                    }
                }
                SCORE_TABLE[row] = score
                var i = 0
                while (i < 3) {
                    var j = i + 1
                    while (j < 4) {
                        if (line[j] != 0L) break
                        ++j
                    }
                    if (j == 4) break
                    if (line[i] == 0L) {
                        line[i] = line[j]
                        line[j] = 0
                        i--
                    } else if (line[i] == line[j] && line[i] != 0xFL) {
                        line[i]++
                        line[j] = 0
                    }
                    ++i
                }

                val result = line[0] or
                    (line[1] shl 4) or
                    (line[2] shl 8) or
                    (line[3] shl 12)

                val rev_result = reverseRow(result.toInt()).toLong()
                val rev_row = reverseRow(row).toLong()

                ROW_LEFT_TABLE[row] = row.toLong() xor result
                ROW_RIGHT_TABLE[rev_row.toInt()] = rev_row xor rev_result
                COL_UP_TABLE[row] =
                    unpackCol(row.toLong()) xor unpackCol(result)
                COL_DOWN_TABLE[rev_row.toInt()] =
                    unpackCol(rev_row) xor unpackCol(rev_result)
            }
        }

        private fun transpose(x: Long): Long {
            val a1 = x and -0xf0ff0f00f0ff0f1L
            val a2 = x and 0x0000F0F00000F0F0L
            val a3 = x and 0x0F0F00000F0F0000L
            val a = a1 or (a2 shl 12) or (a3 shr 12)
            val b1 = a and -0xff00ffff00ff01L
            val b2 = a and 0x00FF00FF00000000L
            val b3 = a and 0x00000000FF00FF00L
            return b1 or (b2 shr 24) or (b3 shl 24)
        }

        private fun executeMoveDown(board: Long): Long {
            var ret = board
            val t = transpose(board)
            ret = ret xor COL_UP_TABLE[(t and ROW_MASK).toInt()]
            ret = ret xor (COL_UP_TABLE[(t shr 16 and ROW_MASK).toInt()]
                shl 4)
            ret = ret xor (COL_UP_TABLE[(t shr 32 and ROW_MASK).toInt()]
                shl 8)
            ret = ret xor (COL_UP_TABLE[(t shr 48 and ROW_MASK).toInt()]
                shl 12)
            return ret
        }

        private fun executeMoveUp(board: Long): Long {
            var ret = board
            val t = transpose(board)
            ret = ret xor COL_DOWN_TABLE[(t and ROW_MASK).toInt()]
            ret = ret xor (COL_DOWN_TABLE[(t shr 16 and ROW_MASK).toInt()]
                shl 4)
            ret = ret xor (COL_DOWN_TABLE[(t shr 32 and ROW_MASK).toInt()]
                shl 8)
            ret = ret xor (COL_DOWN_TABLE[(t shr 48 and ROW_MASK).toInt()]
                shl 12)
            return ret
        }

        private fun executeMoveRight(board: Long): Long {
            var ret = board
            ret = ret xor ROW_LEFT_TABLE[(board and ROW_MASK).toInt()]
            ret = ret xor (ROW_LEFT_TABLE[(board shr 16 and ROW_MASK).toInt()]
                shl 16)
            ret = ret xor (ROW_LEFT_TABLE[(board shr 32 and ROW_MASK).toInt()]
                shl 32)
            ret = ret xor (ROW_LEFT_TABLE[(board shr 48 and ROW_MASK).toInt()]
                shl 48)
            return ret
        }

        private fun executeMoveLeft(board: Long): Long {
            var ret = board
            ret = ret xor ROW_RIGHT_TABLE[(board and ROW_MASK).toInt()]
            ret = ret xor (ROW_RIGHT_TABLE[(board shr 16 and ROW_MASK).toInt()]
                shl 16)
            ret = ret xor (ROW_RIGHT_TABLE[(board shr 32 and ROW_MASK).toInt()]
                shl 32)
            ret = ret xor (ROW_RIGHT_TABLE[(board shr 48 and ROW_MASK).toInt()]
                shl 48)
            return ret
        }

        private fun scoreHelper(board: Long): Int {
            return SCORE_TABLE[(board and ROW_MASK).toInt()] +
                SCORE_TABLE[(board shr 16 and ROW_MASK).toInt()] +
                SCORE_TABLE[(board shr 32 and ROW_MASK).toInt()] +
                SCORE_TABLE[(board shr 48 and ROW_MASK).toInt()]
        }

        private fun unpackCol(row: Long): Long {
            return row or
                (row shl 12) or
                (row shl 24) or
                (row shl 36) and COL_MASK
        }

        private fun reverseRow(row: Int) =
            java.lang.Short.toUnsignedInt((
                row shr 12
                    or (row shr 4 and 0x00F0)
                    or (row shl 4 and 0x0F00)
                    or (row shl 12)).toShort()
            ) // TODO: Do i need the short conversion?
    }
}
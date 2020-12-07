package ru.spbstu

import java.util.*

class SudokuSolverDLX(grid: Array<IntArray>, private val allSolutions: Boolean, private val needToPrint: Boolean) {
    private var board = grid
    private var boardSize = board.size
    private var blockSize = kotlin.math.sqrt(boardSize.toDouble()).toInt()
    private val constraints = 4
    private val coverStartIndex = 1
    private var solvedSudoku = Array(boardSize) { IntArray(boardSize) }
    private var solutionsCount = 0

    fun solve() {
        val cover = initializeExactCoverBoard(board)
        val dlx = DLX(cover, boardSize, allSolutions, needToPrint)
        dlx.runSolver()
        solvedSudoku = dlx.getAnswerGrid()
        solutionsCount = dlx.getSolutionsCount()
    }

    //Getting indices in a cover matrix
    private fun getIndex(row: Int, column: Int, num: Int): Int {
        return (row - 1) * boardSize * boardSize + (column - 1) * boardSize + (num - 1)
    }

    //Creating an empty cover matrix
    private fun createExactCoverBoard(): Array<BooleanArray> {
        val coverBoard = Array(boardSize * boardSize * boardSize) {
            BooleanArray(boardSize * boardSize * constraints)
        }
        var hBase = 0
        hBase = checkCellConstraint(coverBoard, hBase)
        hBase = checkRowConstraint(coverBoard, hBase)
        hBase = checkColumnConstraint(coverBoard, hBase)
        checkSubsectionConstraint(coverBoard, hBase)

        return coverBoard
    }

    /* Checking constraints:
     *  1) Each row will have only one number of each kind
     *  2) Each column will have only one number of each kind
     *  3) Each subsection will have only one number of each kind
     *  4) Only one number can be in a cell
     */

    private fun checkSubsectionConstraint(coverBoard: Array<BooleanArray>, h: Int): Int {
        var hBase = h
        var row = coverStartIndex
        while (row <= boardSize) {
            var column = coverStartIndex
            while (column <= boardSize) {
                var n = coverStartIndex
                while (n <= boardSize) {
                    for (rowDelta in 0 until blockSize) {
                        for (columnDelta in 0 until blockSize) {
                            val index = getIndex(row + rowDelta, column + columnDelta, n)
                            coverBoard[index][hBase] = true
                        }
                    }
                    n++
                    hBase++
                }
                column += blockSize
            }
            row += blockSize
        }
        return hBase
    }

    private fun checkColumnConstraint(coverBoard: Array<BooleanArray>, h: Int): Int {
        var hBase = h
        for (column in coverStartIndex..boardSize) {
            var n = coverStartIndex
            while (n <= boardSize) {
                for (row in coverStartIndex..boardSize) {
                    val index = getIndex(row, column, n)
                    coverBoard[index][hBase] = true
                }
                n++
                hBase++
            }
        }
        return hBase
    }

    private fun checkRowConstraint(coverBoard: Array<BooleanArray>, h: Int): Int {
        var hBase = h
        for (row in coverStartIndex..boardSize) {
            var n = coverStartIndex
            while (n <= boardSize) {
                for (column in coverStartIndex..boardSize) {
                    val index = getIndex(row, column, n)
                    coverBoard[index][hBase] = true
                }
                n++
                hBase++
            }
        }
        return hBase
    }

    private fun checkCellConstraint(coverBoard: Array<BooleanArray>, h: Int): Int {
        var hBase = h
        for (row in coverStartIndex..boardSize) {
            var column = coverStartIndex
            while (column <= boardSize) {
                for (n in coverStartIndex..boardSize) {
                    val index = getIndex(row, column, n)
                    coverBoard[index][hBase] = true
                }
                column++
                hBase++
            }
        }
        return hBase
    }

    // Converting Sudoku grid to a cover matrix
    private fun initializeExactCoverBoard(board: Array<IntArray>): Array<BooleanArray> {
        val coverBoard = createExactCoverBoard()
        for (row in coverStartIndex..boardSize) {
            for (column in coverStartIndex..boardSize) {
                val n = board[row - 1][column - 1]
                if (n != 0) {
                    for (num in 1..boardSize) {
                        if (num != n) {
                            Arrays.fill(coverBoard[getIndex(row, column, num)], false)
                        }
                    }
                }
            }
        }
        return coverBoard
    }

    fun getSolutionsCount(): Int {
        return solutionsCount
    }

    fun saveMatrix(grid: Array<IntArray>) {
        for (row in 0..8) {
            for (column in 0..8) {
                board[row][column] = grid[row][column]
            }
        }
    }

    fun getSolvedGrid(): Array<IntArray> {
        return solvedSudoku
    }
}
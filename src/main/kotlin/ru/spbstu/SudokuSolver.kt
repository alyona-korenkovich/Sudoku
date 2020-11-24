package ru.spbstu

class SudokuSolver(grid: Array<IntArray>) {
    private var board: Array<IntArray> = Array(9) { IntArray(9) }

    init {
        saveMatrix(grid)
    }

    fun solve(): Boolean {
        for (row in 0 until 9) {
            for (column in 0 until 9) {
                if (board[row][column] == 0) {
                    for (k in 1..9) {
                        board[row][column] = k
                        if (validityCheck(board, row, column) && solve()) {
                            return true
                        }
                        board[row][column] = 0
                    }
                    return false
                }
            }
        }
        return true
    }

    fun validityCheck(board: Array<IntArray>, row: Int, column: Int): Boolean {
        return isInRow(board, row) && isInColumn(board, column) && isInBlock(board, row, column)
    }

    private fun isInRow(board: Array<IntArray>, row: Int): Boolean {
        val digits = BooleanArray(9)
        for (column in 0 until 9) {
            if (!checkConstraint(board, row, digits, column)) return false
        }
        return true
    }

    private fun isInColumn(board: Array<IntArray>, column: Int): Boolean {
        val digits = BooleanArray(9)
        for (row in 0 until 9) {
            if (!checkConstraint(board, row, digits, column)) return false
        }
        return true
    }

    private fun isInBlock(board: Array<IntArray>, row: Int, column: Int): Boolean {
        val digits = BooleanArray(9)
        val subsectionRowStart: Int = row / 3 * 3
        val subsectionRowEnd: Int = subsectionRowStart + 3
        val subsectionColumnStart: Int = column / 3 * 3
        val subsectionColumnEnd: Int = subsectionColumnStart + 3
        for (r in subsectionRowStart until subsectionRowEnd) {
            for (c in subsectionColumnStart until subsectionColumnEnd) {
                if (!checkConstraint(board, r, digits, c)) return false
            }
        }
        return true
    }

    private fun checkConstraint(board: Array<IntArray>, row: Int, constraint: BooleanArray, column: Int): Boolean {
        if (board[row][column] != 0) {
            if (!constraint[board[row][column] - 1]) {
                constraint[board[row][column] - 1] = true
            } else return false
        }
        return true
    }

    fun getBoard(): Array<IntArray> {
        return board
    }

    fun saveMatrix(grid: Array<IntArray>) {
        for (row in 0..8) {
            for (column in 0..8) {
                board[row][column] = grid[row][column]
            }
        }
    }
}
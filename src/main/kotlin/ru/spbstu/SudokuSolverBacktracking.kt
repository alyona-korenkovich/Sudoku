package ru.spbstu

/** Solving Sudoku using Backtracking Algorithm
 *
 * If there's no constraints' violation, the algorithm goes to the next cell,
 * fills in all the possible solutions and repeat all the checks.
 * If there's at least one violation of the constraints, the number of a cell increases
 * As far as the number gets to 9 and constraints' violation remains,
 * algorithm backtracks to the previous cell and increases its number.
 */


class SudokuSolverBacktracking(grid: Array<IntArray>) {
    private var gridSize = grid.size
    private var board = Array(gridSize) { IntArray(gridSize) }
    private var boardSize = gridSize
    private var blockSize = kotlin.math.sqrt(boardSize.toDouble()).toInt()

    init {
        saveMatrix(grid)
    }

    fun solve(): Boolean {
        for (row in 0 until boardSize) {
            for (column in 0 until boardSize) {
                if (board[row][column] == 0) {
                    for (k in 1..boardSize) {
                        board[row][column] = k
                        if (validityCheck(board, row, column) && solve()) {
                            return true
                        }
                        //Backtracking if reaching a dead end
                        board[row][column] = 0
                    }
                    return false
                }
            }
        }
        return true
    }

    /** Checking constraints:
     *  1) Each row will have only one number of each kind
     *  2) Each column will have only one number of each kind
     *  3) Each subsection will have only one number of each kind
     *  4) Only one number can be in a cell
     */

    fun validityCheck(board: Array<IntArray>, row: Int, column: Int): Boolean {
        return isInRow(board, row) && isInColumn(board, column) && isInBlock(board, row, column)
    }

    private fun isInRow(board: Array<IntArray>, row: Int): Boolean {
        val digits = BooleanArray(boardSize)
        for (column in 0 until boardSize) {
            if (!checkConstraint(board, row, digits, column)) return false
        }
        return true
    }

    private fun isInColumn(board: Array<IntArray>, column: Int): Boolean {
        val digits = BooleanArray(boardSize)
        for (row in 0 until boardSize) {
            if (!checkConstraint(board, row, digits, column)) return false
        }
        return true
    }

    private fun isInBlock(board: Array<IntArray>, row: Int, column: Int): Boolean {
        val digits = BooleanArray(boardSize)
        val subsectionRowStart: Int = row / blockSize * blockSize
        val subsectionRowEnd: Int = subsectionRowStart + blockSize
        val subsectionColumnStart: Int = column / blockSize * blockSize
        val subsectionColumnEnd: Int = subsectionColumnStart + blockSize
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

    //Helper functions

    fun getBoard(): Array<IntArray> {
        return board
    }

    private fun saveMatrix(grid: Array<IntArray>) {
        for (row in 0 until boardSize) {
            for (column in 0 until boardSize) {
                board[row][column] = grid[row][column]
            }
        }
    }
}
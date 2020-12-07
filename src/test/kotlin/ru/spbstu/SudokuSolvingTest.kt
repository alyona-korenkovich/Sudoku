package ru.spbstu

import org.junit.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuSolvingTest {
    private lateinit var sudoku: Sudoku
    private lateinit var sudokuSolverBacktracking: SudokuSolverBacktracking
    private lateinit var sudokuSolverDLX: SudokuSolverDLX
    private lateinit var grid: Array<IntArray>
    private lateinit var answerGrid: Array<IntArray>

    private fun createTestGrid9x9(): Array<IntArray> {
        val grid = Array(9) { IntArray(9) }
        grid[0] = intArrayOf(8, 0, 0, 0, 0, 0, 0, 0, 0)
        grid[1] = intArrayOf(0, 0, 3, 6, 0, 0, 0, 0, 0)
        grid[2] = intArrayOf(0, 7, 0, 0, 9, 0, 2, 0, 0)
        grid[3] = intArrayOf(0, 5, 0, 0, 0, 7, 0, 0, 0)
        grid[4] = intArrayOf(0, 0, 0, 0, 4, 5, 7, 0, 0)
        grid[5] = intArrayOf(0, 0, 0, 1, 0, 0, 0, 3, 0)
        grid[6] = intArrayOf(0, 0, 1, 0, 0, 0, 0, 6, 8)
        grid[7] = intArrayOf(0, 0, 8, 5, 0, 0, 0, 1, 0)
        grid[8] = intArrayOf(0, 9, 0, 0, 0, 0, 4, 0, 0)
        return grid
    }
    private fun createTestGrid16x16(): Array<IntArray> {
        val grid = Array(16) { IntArray(16) }
        grid[0] = intArrayOf(16, 0, 4, 15, 0, 0, 0, 0, 1, 2, 0, 0, 12, 0, 6, 3)
        grid[1] = intArrayOf(6, 0, 7, 10, 3, 2, 11, 16, 0, 12, 0, 0, 0, 0, 0, 0)
        grid[2] = intArrayOf(0, 0, 0, 0, 6, 0, 0, 0, 7, 3, 15, 9, 16, 0, 2, 8)
        grid[3] = intArrayOf(1, 0, 3, 0, 12, 0, 0, 14, 0, 6, 0, 0, 0, 0, 4, 9)
        grid[4] = intArrayOf(0, 0, 9, 0, 0, 3, 0, 0, 14, 0, 12, 0, 1, 2, 10, 0)
        grid[5] = intArrayOf(0, 0, 2, 0, 4, 0, 9, 7, 15, 5, 0, 10, 0, 0, 11, 0)
        grid[6] = intArrayOf(15, 14, 5, 6, 0, 10, 0, 0, 2, 0, 13, 0, 0, 0, 3, 0)
        grid[7] = intArrayOf(13, 0, 12, 0, 1, 6, 2, 0, 0, 0, 8, 0, 9, 0, 14, 0)
        grid[8] = intArrayOf(0, 4, 0, 7, 0, 12, 0, 0, 0, 13, 1, 3, 0, 15, 0, 11)
        grid[9] = intArrayOf(0, 13, 0, 0, 0, 16, 0, 4, 0, 0, 6, 0, 3, 9, 5, 2)
        grid[10] = intArrayOf(0, 16, 0, 0, 15, 0, 8, 2, 9, 11, 0, 4, 0, 13, 0, 0)
        grid[11] = intArrayOf(0, 6, 15, 5, 0, 11, 0, 10, 0, 0, 14, 0, 0, 8, 0, 0)
        grid[12] = intArrayOf(14, 7, 0, 0, 0, 0, 10, 0, 13, 0, 0, 12, 0, 11, 0, 16)
        grid[13] = intArrayOf(12, 15, 0, 11, 16, 8, 6, 9, 0, 0, 0, 5, 0, 0, 0, 0)
        grid[14] = intArrayOf(0, 0, 0, 0, 0, 0, 14, 0, 11, 10, 3, 16, 6, 12, 0, 4)
        grid[15] = intArrayOf(4, 3, 0, 1, 0, 0, 12, 11, 0, 0, 0, 0, 2, 14, 0, 10)
        return grid
    }
    private fun createAnswerGrid9x9(): Array<IntArray> {
        val answer = Array(9) { IntArray(9) }
        answer[0] = intArrayOf(8, 1, 2, 7, 5, 3, 6, 4, 9)
        answer[1] = intArrayOf(9, 4, 3, 6, 8, 2, 1, 7, 5)
        answer[2] = intArrayOf(6, 7, 5, 4, 9, 1, 2, 8, 3)
        answer[3] = intArrayOf(1, 5, 4, 2, 3, 7, 8, 9, 6)
        answer[4] = intArrayOf(3, 6, 9, 8, 4, 5, 7, 2, 1)
        answer[5] = intArrayOf(2, 8, 7, 1, 6, 9, 5, 3, 4)
        answer[6] = intArrayOf(5, 2, 1, 9, 7, 4, 3, 6, 8)
        answer[7] = intArrayOf(4, 3, 8, 5, 2, 6, 9, 1, 7)
        answer[8] = intArrayOf(7, 9, 6, 3, 1, 8, 4, 5, 2)
        return answer
    }
    private fun createAnswerGrid16x16(): Array<IntArray> {
        val grid = Array(16) { IntArray(16) }
        grid[0] = intArrayOf(16, 5, 4, 15, 10, 9, 13, 8, 1, 2, 11, 14, 12, 7, 6, 3)
        grid[1] = intArrayOf(6, 9, 7, 10, 3, 2, 11, 16, 5, 12, 4, 8, 15, 1, 13, 14)
        grid[2] = intArrayOf(11, 12, 14, 13, 6, 5, 4, 1, 7, 3, 15, 9, 16, 10, 2, 8)
        grid[3] = intArrayOf(1, 8, 3, 2, 12, 15, 7, 14, 16, 6, 10, 13, 11, 5, 4, 9)
        grid[4] = intArrayOf(7, 11, 9, 16, 8, 3, 15, 13, 14, 4, 12, 6, 1, 2, 10, 5)
        grid[5] = intArrayOf(8, 1, 2, 3, 4, 14, 9, 7, 15, 5, 16, 10, 13, 6, 11, 12)
        grid[6] = intArrayOf(15, 14, 5, 6, 11, 10, 16, 12, 2, 9, 13, 1, 8, 4, 3, 7)
        grid[7] = intArrayOf(13, 10, 12, 4, 1, 6, 2, 5, 3, 7, 8, 11, 9, 16, 14, 15)
        grid[8] = intArrayOf(2, 4, 8, 7, 9, 12, 5, 6, 10, 13, 1, 3, 14, 15, 16, 11)
        grid[9] = intArrayOf(10, 13, 11, 12, 14, 16, 1, 4, 8, 15, 6, 7, 3, 9, 5, 2)
        grid[10] = intArrayOf(3, 16, 1, 14, 15, 7, 8, 2, 9, 11, 5, 4, 10, 13, 12, 6)
        grid[11] = intArrayOf(9, 6, 15, 5, 13, 11, 3, 10, 12, 16, 14, 2, 4, 8, 7, 1)
        grid[12] = intArrayOf(14, 7, 6, 8, 2, 4, 10, 3, 13, 1, 9, 12, 5, 11, 15, 16)
        grid[13] = intArrayOf(12, 15, 10, 11, 16, 8, 6, 9, 4, 14, 2, 5, 7, 3, 1, 13)
        grid[14] = intArrayOf(5, 2, 13, 9, 7, 1, 14, 15, 11, 10, 3, 16, 6, 12, 8, 4)
        grid[15] = intArrayOf(4, 3, 16, 1, 5, 13, 12, 11, 6, 8, 7, 15, 2, 14, 9, 10)
        return grid
    }
    private fun createWrongGrid(): Array<IntArray> {
        val wrong = Array(9) { IntArray(9) }

        wrong[0] = intArrayOf(9, 1, 2, 7, 5, 3, 6, 4, 9)
        wrong[1] = intArrayOf(9, 4, 3, 6, 8, 2, 1, 7, 5)
        wrong[2] = intArrayOf(6, 7, 5, 4, 9, 1, 2, 8, 3)
        wrong[3] = intArrayOf(1, 5, 4, 2, 3, 7, 8, 9, 6)
        wrong[4] = intArrayOf(3, 6, 9, 8, 4, 5, 7, 2, 1)
        wrong[5] = intArrayOf(2, 8, 7, 1, 6, 9, 5, 3, 4)
        wrong[6] = intArrayOf(5, 2, 1, 9, 7, 4, 3, 6, 8)
        wrong[7] = intArrayOf(4, 3, 8, 5, 2, 6, 9, 1, 7)
        wrong[8] = intArrayOf(7, 9, 6, 3, 1, 8, 4, 5, 2)

        return wrong
    }

    @Test
    fun sudokuSolvingTest() {
        //Checking sudokuSolverBacktracking.solve() and sudokuSolverDLX.solve() on grids 9x9 & 16x16

        grid = createTestGrid9x9()
        answerGrid = createAnswerGrid9x9()
        sudokuSolverBacktracking = SudokuSolverBacktracking(grid)
        var timeBacktracking: Long = measureTimeMillis { sudokuSolverBacktracking.solve() } //time in milliseconds
        var solved = sudokuSolverBacktracking.getBoard()
        for (row in 0 until 9) {
            assertTrue(answerGrid[row].contentEquals(solved[row]))
        }

        sudokuSolverDLX = SudokuSolverDLX(grid, false, needToPrint = true)
        var timeDLX: Long = measureTimeMillis { sudokuSolverDLX.solve() }
        solved = sudokuSolverDLX.getSolvedGrid()
        for (row in 0 until 9) {
            assertTrue(answerGrid[row].contentEquals(solved[row]))
        }

        println("Time solving (Backtracking): $timeBacktracking")
        println("Time solving (DLX): $timeDLX")

        grid = createTestGrid16x16()
        answerGrid = createAnswerGrid16x16()
        sudokuSolverBacktracking = SudokuSolverBacktracking(grid)
        timeBacktracking = measureTimeMillis { sudokuSolverBacktracking.solve() }
        solved = sudokuSolverBacktracking.getBoard()
        for (row in 0 until 16) {
            assertTrue(answerGrid[row].contentEquals(solved[row]))
        }

        sudokuSolverDLX = SudokuSolverDLX(grid, false, needToPrint = true)
        timeDLX = measureTimeMillis { sudokuSolverDLX.solve() }
        solved = sudokuSolverDLX.getSolvedGrid()
        for (row in 0 until 9) {
            assertTrue(answerGrid[row].contentEquals(solved[row]))
        }

        println("Time solving (Backtracking): $timeBacktracking")
        println("Time solving (DLX): $timeDLX")
    }

    /* Time experiment.
     Created to check the dependence of time on the complexity of Sudoku.
     The result is displayed as "Difficulty - Time"

     Sudoku taken for the experiment - 9x9.
     */

    @Test
    fun timeExperiment() {
        var timeInMillis: Long
        var timeInMillisMax: Long = -1
        var complexGrid = Array(9) { IntArray(9) }
        var answerForComplexGrid = Array(9) { IntArray(9) }
        val averageMap = mutableMapOf<Int, List<Long>>() //Number of empty cells - test times solving

        //****** BACKTRACKING ALGORITHM ******
        for (i in 0..80) {
            //taking 10 tests in order to calculate an average time solving for each difficulty level
            val list = mutableListOf<Long>()
            for (j in 0..9) {
                sudoku = Sudoku(9, i)
                val tempGrid = sudoku.getGrid()
                timeInMillis = measureTimeMillis {
                    sudokuSolverBacktracking = SudokuSolverBacktracking(sudoku.getGrid())
                    sudokuSolverBacktracking.solve()
                }
                list.add(timeInMillis)
                if (timeInMillis > timeInMillisMax) {
                    timeInMillisMax = timeInMillis
                    complexGrid = tempGrid
                    answerForComplexGrid = sudokuSolverBacktracking.getBoard()
                }
            }
            averageMap[i] = list
        }

        println("Average time solving:")
        for (i in averageMap.keys) {
            val currAverage = averageMap[i]!!.average()
            println("$i -- $currAverage")
        }
        println("The most complex sudoku to solve: ")
        sudoku.printTheGrid(complexGrid)
        println("Its solution: ")
        sudoku.printTheGrid(answerForComplexGrid)

        //****** DLX ALGORITHM ******

        for (i in 1..80) {
            val list = mutableListOf<Long>()
            for (j in 0..9) {
                sudoku = Sudoku(9, i)
                val tempGrid = sudoku.getGrid()
                timeInMillis = measureTimeMillis {
                    sudokuSolverDLX = SudokuSolverDLX(sudoku.getGrid(), false, needToPrint = false)
                    sudokuSolverDLX.solve()
                }
                list.add(timeInMillis)
                if (timeInMillis > timeInMillisMax) {
                    timeInMillisMax = timeInMillis
                    complexGrid = tempGrid
                    answerForComplexGrid = sudokuSolverDLX.getSolvedGrid()
                }
            }
            averageMap[i] = list
        }

        println("Average time solving:")
        for (i in averageMap.keys) {
            val currAverage = averageMap[i]!!.average()
            println("$i - $currAverage")
        }
        println("The most complex sudoku to solve: ")
        sudoku.printTheGrid(complexGrid)
        println("Its solution: ")
        sudoku.printTheGrid(answerForComplexGrid)
    }

    /* Checking the dependence of the time it takes to solve a sudoku on its size
    Grid sizes taken for the experiment - 9x9, 16x16, 25x25, 36x36
    Difficulty - 70%
     */

    @Test
    fun sizeIncreasingTest() {
        val sudokuSizes = listOf(9, 16, 25, 36)

        //Testing DLX Algorithm
        for (sudokuSize in sudokuSizes) {
            val difficulty = (0.7 * sudokuSize * sudokuSize).toInt()
            sudoku = Sudoku(sudokuSize, difficulty)
            sudokuSolverDLX = SudokuSolverDLX(sudoku.getGrid(), false, needToPrint = true)
            val timeInMillis = measureTimeMillis {
                sudokuSolverDLX.solve()
            }
            println("($sudokuSize x $sudokuSize) -- $timeInMillis")
        }

        //Testing Backtracking Algorithm
        for (sudokuSize in sudokuSizes) {
            val difficulty = (0.7 * sudokuSize * sudokuSize).toInt()
            sudoku = Sudoku(sudokuSize, difficulty)
            sudokuSolverBacktracking = SudokuSolverBacktracking(sudoku.getGrid())
            val timeInMillis = measureTimeMillis {
                sudokuSolverBacktracking.solve()
            }
            println("($sudokuSize x $sudokuSize) -- $timeInMillis")
        }
    }

    @Test
    fun gridIsValid() {
        grid = createTestGrid9x9()
        sudokuSolverBacktracking = SudokuSolverBacktracking(grid)
        var isValid = true

        for (row in 0..8) {
            for (column in 0..8) {
                val res = sudokuSolverBacktracking.validityCheck(grid, row, column)
                if (!res) {
                    isValid = false
                    break
                }
            }
        }
        assertTrue(isValid)

        grid = createWrongGrid()
        sudokuSolverBacktracking = SudokuSolverBacktracking(grid)
        isValid = false
        for (row in 0..8) {
            for (column in 0..8) {
                val res = sudokuSolverBacktracking.validityCheck(grid, row, column)
                if (!res) {
                    isValid = false
                    break
                }
            }
        }
        assertFalse(isValid)
    }

    @Test
    fun shuffleTest() {
        sudoku = Sudoku(9, 21)
        grid = sudoku.getGrid()
        sudoku.shuffle(grid)
        val test = sudoku.getGrid()
        val init = Array (9) { IntArray (9) }
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                init[i][j] = (i * 3 + i / 3 + j) % 3 + 1
            }
        }

        var isShuffled = false
        for (row in 0..8) {
            if (test[row].contentToString() != init[row].contentToString()) {
                isShuffled = true
                break
            }
        }
        assertTrue(isShuffled)
    }

    @Test
    fun removeCellsTest() {
        val difficulty = 8
        var cntEmptyCells = 0
        sudoku = Sudoku(9, difficulty)

        /*When initializing sudoku, method 'setUpGrid' runs and removes cells automatically
        We will just be checking that there're '0' in the generated sudoku grid
         */

        val test = sudoku.getGrid()
        val gridSize = test.size
        for (row in 0 until gridSize) {
            for (column in 0 until gridSize) {
                if (test[row][column] == 0) cntEmptyCells++
            }
        }
        sudoku.printTheGrid(test)

        assertEquals(difficulty, cntEmptyCells)
    }
}

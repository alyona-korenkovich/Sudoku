package ru.spbstu

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SudokuSolvingTest {
    private val sudoku = Sudoku()
    private lateinit var sudokuSolver: SudokuSolver
    private val grid = createTestGrid()
    private val incorrectGrid = createWrongGrid()
    private val answer: Array<IntArray> = createAnswerGrid()

    private fun createTestGrid(): Array<IntArray> {
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

    private fun createAnswerGrid(): Array<IntArray> {
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

    private fun createWrongGrid(): Array<IntArray> {
        val wrong = Array(9) { IntArray(9) }

        wrong[0] = intArrayOf(9, 1, 2, 7, 5, 3, 6, 4, 9) //Две девятки в одном ряду, колонке и блоке
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
        sudokuSolver = SudokuSolver(grid)
        sudokuSolver.solve()
        val solved = sudokuSolver.getBoard()
        for (row in 0 until 9) {
            assertTrue(answer[row].contentEquals(solved[row]))
        }
    }

    @Test
    fun gridIsValid() {
        sudokuSolver = SudokuSolver(grid)
        var isValid = true

        for (row in 0..8) {
            for (column in 0..8) {
                val res = sudokuSolver.validityCheck(grid, row, column)
                if (!res) {
                    isValid = false
                    break
                }
            }
        }
        assertTrue(isValid)

        sudokuSolver = SudokuSolver(incorrectGrid)
        isValid = false
        for (row in 0..8) {
            for (column in 0..8) {
                val res = sudokuSolver.validityCheck(incorrectGrid, row, column)
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
        val test = sudoku.shuffle(grid)
        var isShuffled = false
        for (row in 0..8) {
            if (test[row].contentToString() != grid[row].contentToString()) {
                isShuffled = true
                break
            }
        }
        assertTrue(isShuffled)
    }

    @Test
    fun removeCellsTest() {
        val test = createAnswerGrid()
        val difficulty = 49
        var cntEmptyCells = 0
        sudoku.removeCells(test, difficulty)
        for (row in 0..8) {
            for (column in 0..8) {
                if (test[row][column] == 0) cntEmptyCells++
            }
        }
        assertEquals(difficulty, cntEmptyCells)
    }
}

package ru.spbstu

import java.lang.StringBuilder
import kotlin.math.sqrt
import kotlin.random.Random

class Sudoku(gridSize: Int, difficulty: Int) {
    private var boardSize = gridSize
    private var blockSize = sqrt(boardSize.toDouble()).toInt()
    private var board: Array<IntArray> = Array(boardSize) { IntArray(boardSize) }
    private var n = difficulty

    init {
        setUpGrid()
    }

    //Generation of Sudoku: getting complete grid, its shuffling and removing random number of cells
    private fun setUpGrid(): Array<IntArray> {
        createBaseGrid()
        board = shuffle(board)
        removeCells(board, n)
        return board
    }

    //Complete grid
    private fun createBaseGrid() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                board[i][j] = (i * blockSize + i / blockSize + j) % boardSize + 1
            }
        }
    }

    //Shuffling the grid by calling the functions randomly
    fun shuffle(board: Array<IntArray>): Array<IntArray> {
        var shuffledGrid = board

        var num = Random.nextInt(boardSize, blockSize * boardSize)
        val randI = Random.nextInt(boardSize, num + 1)
        while (num != 0) {
            if (num == randI) {
                shuffledGrid = transpose(shuffledGrid)
            }
            when (Random.nextInt(1, 5)) {
                1 -> swapRowsSmall(shuffledGrid)
                2 -> shuffledGrid = swapColumnsSmall(shuffledGrid)
                3 -> swapRowsAreas(shuffledGrid)
                else -> shuffledGrid = swapColumnsAreas(shuffledGrid)
            }
            num--
        }
        return shuffledGrid
    }

    //Exchanging the positions of rows and columns
    private fun transpose(grid: Array<IntArray>): Array<IntArray> {
        val transposedGrid = Array(boardSize) { IntArray(boardSize) }
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                transposedGrid[i][j] = grid[j][i]
            }
        }
        return transposedGrid
    }

    //Swapping two rows belonging to one area
    private fun swapRowsSmall(grid: Array<IntArray>) {
        val randBlock = Random.nextInt(0, blockSize)
        val randRow1 = blockSize * randBlock + Random.nextInt(0, blockSize)
        var randRow2 = blockSize * randBlock + Random.nextInt(0, blockSize)
        //In case if randRow1 == randRow2, we rerandomize the second row
        while (randRow1 == randRow2) {
            randRow2 = blockSize * randBlock + Random.nextInt(0, blockSize)
        }
        val temp = grid[randRow1]
        grid[randRow1] = grid[randRow2]
        grid[randRow2] = temp
    }

    //Swapping several rows (areas)
    private fun swapRowsAreas(grid: Array<IntArray>) {
        val firstArea = Random.nextInt(0, blockSize)
        var secondArea = Random.nextInt(0, blockSize)
        while (firstArea == secondArea) {
            secondArea = Random.nextInt(0, blockSize)
        }
        for (i in 0 until blockSize) {
            val m = firstArea * blockSize + i
            val n = secondArea * blockSize + i
            val temp = grid[m]
            grid[m] = grid[n]
            grid[n] = temp
        }
    }

    //Swapping two columns belonging to one area
    private fun swapColumnsSmall(grid: Array<IntArray>): Array<IntArray> {
        val tempGrid = transpose(grid)
        swapRowsSmall(tempGrid)
        return transpose(tempGrid)
    }

    //Swapping several columns (areas)
    private fun swapColumnsAreas(grid: Array<IntArray>): Array<IntArray> {
        val tempGrid = transpose(grid)
        swapRowsAreas(tempGrid)
        return transpose(tempGrid)
    }

    //Removal of 'n' cells
    private fun removeCells(grid: Array<IntArray>, n: Int) {
        val seen = Array(boardSize) { IntArray(boardSize) { 0 } }
        var difficulty = n
        val solvedGrid = Array(boardSize) { IntArray(boardSize) }
        val solver = SudokuSolverDLX(grid, allSolutions = false, needToPrint = false)

        for (row in 0 until boardSize) {
            for (column in 0 until boardSize) {
                solvedGrid[row][column] = grid[row][column]
            }
        }

        while (difficulty != 0) {
            val randRow = Random.nextInt(0, boardSize)
            val randColumn = Random.nextInt(0, boardSize)
            if (seen[randRow][randColumn] == 0) {
                seen[randRow][randColumn] = 1
                val selectedCell = grid[randRow][randColumn] //Saving cell
                grid[randRow][randColumn] = 0
                solver.saveMatrix(grid)
                solver.solve()
                if (solver.getSolutionsCount() != 1) {
                    grid[randRow][randColumn] = selectedCell
                    seen[randRow][randColumn] = 0
                } else
                    difficulty--
            }
        }
    }

    //Helper functions

    fun printTheGrid(grid: Array<IntArray>) {
        val size = grid.size
        for (row in grid) {
            val sB = StringBuilder()
            for (i in 0 until size) {
                sB.append(row[i]).append(" ")
            }
            println(sB)
        }
        println()
    }

    fun getGrid(): Array<IntArray> {
        return board
    }
}
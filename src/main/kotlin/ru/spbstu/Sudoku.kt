package ru.spbstu

import kotlin.random.Random

class Sudoku {
    private var board: Array<IntArray> = Array(9) { IntArray(9) }

    fun setUpGrid(): Array<IntArray> {
        createBaseGrid()
        board = shuffle(board)
        removeCells(board, Random.nextInt(30, 70))
        return board
    }

    private fun createBaseGrid() {
        for (i in 0..8) {
            for (j in 0..8) {
                board[i][j] = (i * 3 + i / 3 + j) % 9 + 1
            }
        }
    }

    fun shuffle(board: Array<IntArray>): Array<IntArray> {
        var shuffledGrid = board

        var num = Random.nextInt(9, 28)
        val randI = Random.nextInt(9, num + 1)
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

    private fun transpose(grid: Array<IntArray>): Array<IntArray> {
        val transposedGrid = Array(9) { IntArray(9) }
        for (i in 0..8) {
            for (j in 0..8) {
                transposedGrid[i][j] = grid[j][i]
            }
        }
        return transposedGrid
    }

    private fun swapRowsSmall(grid: Array<IntArray>) {
        val firstRow = Random.nextInt(0, 9)
        val secondRow = when (firstRow % 3) {
            0 -> firstRow + Random.nextInt(1, 3)
            1 -> firstRow + Random.nextInt(0, 2) * 2 - 1
            else -> firstRow + Random.nextInt(0, 2) - 2
        }
        val temp = grid[firstRow]
        grid[firstRow] = grid[secondRow]
        grid[secondRow] = temp
    }

    private fun swapRowsAreas(grid: Array<IntArray>) {
        val firstArea = Random.nextInt(0, 3)
        var secondArea = Random.nextInt(0, 3)
        while (firstArea == secondArea) {
            secondArea = Random.nextInt(0, 3)
        }
        for (i in 0..2) {
            val m = firstArea * 3 + i
            val n = secondArea * 3 + i
            val temp = grid[m]
            grid[m] = grid[n]
            grid[n] = temp
        }
    }

    private fun swapColumnsSmall(grid: Array<IntArray>): Array<IntArray> {
        val tempGrid = transpose(grid)
        swapRowsSmall(tempGrid)
        return transpose(tempGrid)
    }

    private fun swapColumnsAreas(grid: Array<IntArray>): Array<IntArray> {
        val tempGrid = transpose(grid)
        swapRowsAreas(tempGrid)
        return transpose(tempGrid)
    }

    fun removeCells(grid: Array<IntArray>, n: Int) {
        val seen = Array(9) { IntArray(9) { 0 } }
        var difficulty = n
        val solver = SudokuSolver(grid)
        val solvedGrid = Array(9) { IntArray(9) }

        for (row in 0..8) {
            for (column in 0..8) {
                solvedGrid[row][column] = grid[row][column]
            }
        }

        while (difficulty != 0) {
            val randRow = Random.nextInt(0, 9)
            val randColumn = Random.nextInt(0, 9)
            if (seen[randRow][randColumn] == 0) {
                seen[randRow][randColumn] = 1
                val selectedCell = grid[randRow][randColumn]
                grid[randRow][randColumn] = 0
                solver.saveMatrix(grid)
                solver.solve()
                if (compareTwoMatrices(solvedGrid, solver.getBoard())) difficulty--
                else grid[randRow][randColumn] = selectedCell
            }
        }
    }

    private fun compareTwoMatrices(grid: Array<IntArray>, grid2: Array<IntArray>): Boolean {
        for (row in 0..8) {
            if (grid[row].contentToString() != grid2[row].contentToString()) {
                return false
            }
        }
        return true
    }

    fun printTheGrid(grid: Array<IntArray>) {
        for (row in 0 until 9) {
            println(grid[row].contentToString())
        }
    }
}
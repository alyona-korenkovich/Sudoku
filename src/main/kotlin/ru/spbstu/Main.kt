package ru.spbstu

import kotlin.system.measureTimeMillis

fun main() {
    val time: Long
    val sudoku = Sudoku(16, 180)
    val grid = sudoku.getGrid()
    println("Your initial grid:")
    sudoku.printTheGrid(grid)

    val sudokuSolver = SudokuSolverDLX(grid, false, needToPrint = true)
    println("Your solved grid:")
    time = measureTimeMillis {
        sudokuSolver.solve()
    }
    println("Timing: $time milliseconds")
}
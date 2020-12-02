package ru.spbstu

import kotlin.system.measureTimeMillis

/**
 * Судоку. Правила:
 *
 * Игровое поле представляет собой квадрат размером N×N, разделённый на меньшие квадраты со стороной в 3 клетки.
 * Таким образом, всё игровое поле состоит из N^2 клеток.
 * В них уже в начале игры стоят некоторые числа (от 1 до N), называемые подсказками.
 * От игрока требуется заполнить свободные клетки цифрами от 1 до N так, чтобы в каждой строке,
 * в каждом столбце и в каждом малом квадрате sqrt(N)×sqrt(N) каждая цифра встречалась бы только один раз.
 *
 *
 * Ограничения судоку:
 *
 * 1) Каждая строка должна иметь каждую цифру от 1 до N только один раз
 * 2) Каждая колонка должна иметь каждую цифру от 1 до N только один раз
 * 3) В каждой секции sqrt(N)×sqrt(N) каждая цифра от 1 до N должна встречаться один раз
 */

fun main() {
    val time: Long
    val sudoku = Sudoku(16, 124)
    val grid = sudoku.getGrid()
    val gridSize = sudoku.getGridSize()
    println("Your initial grid:")
    sudoku.printTheGrid(grid)

    val sudokuSolver = SudokuSolver(grid, gridSize)
    time = measureTimeMillis { sudokuSolver.solve() }
    println("Your solved grid:")
    sudoku.printTheGrid(sudokuSolver.getBoard())
    println("Timing: $time milliseconds")
}
package ru.spbstu

import java.util.*

/** Implementation of Algorithm X
 *
 *  Concept points:
 *  - If the matrix A has no columns, the current partial solution is a valid solution;
 *  terminate successfully, otherwise, choose a column c (deterministically)
 *  - Choose a row r such that Ar, c = 1 (non-deterministically)
 *  - Include row r in the partial solution
 *  - For each column j such that Ar, j = 1, for each row i such that Ai, j = 1,
 *  delete row i from matrix A and delete column j from matrix A
 *  - Repeat this algorithm recursively on the reduced matrix A
 *
 *  -- DLX algorithm was suggested in 2000 by Dr. Donald Knuth
 */

class DLX(cover: Array<BooleanArray>, size: Int, private val allSolutions: Boolean, private val needToPrint: Boolean) {
    private val header: ColumnNode?
    private var answer: MutableList<DancingNode>? = null
    private val boardSize = size
    private var answerGrid = Array(size) { IntArray(size) }
    private var solutionsCount = 0
    private var hasOneSolution = false

    init {
        header = makeDLXBoard(cover)
    }

    //Recursively searching for exact covers (answers)
    private fun search(k: Int) {
        //If there're no columns left, find the solution
        if (header!!.right == header) {
            getSolution(answer)
        } else {
            //Deterministically choose column
            var c = getMinColumn()
            //Cover chosen column
            c!!.cover()
            var r = c.down
            while (r !== c) {
                //Adding 'r' to partial solution
                answer!!.add(r)

                //Covering columns
                var i = r.right
                while (i !== r) {
                    i.column.cover()
                    i = i.right
                }

                //Recursively move to level k + 1
                search(k + 1)
                if (!allSolutions && hasOneSolution) return

                /*
                If a solution is not possible, backtrack (uncover)
                and remove the selected row (set) from the solution
                 */
                r = answer!!.removeAt(answer!!.size - 1)
                c = r.column

                //Uncovering columns
                var j = r.left
                while (j !== r) {
                    j.column.uncover()
                    j = j.left
                }
                r = r.down
            }
            c.uncover()
        }
    }

    //Searching the column with minimum node count (size)
    private fun getMinColumn(): ColumnNode? {
        var min = Int.MAX_VALUE
        var minColumn: ColumnNode? = null
        var currColumn: ColumnNode = header!!.right as ColumnNode
        //Going through all the columns
        while (currColumn != header) {
            if (currColumn.size < min) {
                min = currColumn.size
                minColumn = currColumn
            }
            currColumn = currColumn.right as ColumnNode
        }
        return minColumn
    }

    //Creating the quadruple-chained list representing the previously generated cover matrix
    private fun makeDLXBoard(grid: Array<BooleanArray>): ColumnNode? {
        val columns: Int = grid[0].size
        var headerNode: ColumnNode? = ColumnNode("header")
        val columnNodes: MutableList<ColumnNode> = ArrayList()
        for (i in 0 until columns) {
            val n = ColumnNode(i.toString())
            columnNodes.add(n)
            headerNode = headerNode!!.linkRight(n) as ColumnNode
        }
        headerNode = headerNode!!.right.column
        for (aGrid in grid) {
            var prev: DancingNode? = null
            for (j in 0 until columns) {
                if (aGrid[j]) {
                    val col = columnNodes[j]
                    val newNode = DancingNode()
                    newNode.column = col
                    if (prev == null) prev = newNode
                    col.up.linkDown(newNode)
                    prev = prev.linkRight(newNode)
                    col.size++
                }
            }
        }
        headerNode.size = columns
        return headerNode
    }

    fun runSolver() {
        answer = LinkedList()
        search(0)
    }

    private fun getSolution(answer: List<DancingNode>?) {
        //Increasing count each time when calling getSolution
        solutionsCount++
        val res = parseBoard(answer)
        //Saving the first solution only
        if (solutionsCount == 1) {
            answerGrid = res
            hasOneSolution = true
            if (!allSolutions) {
                printSolution(res)
            }
        } else {
            hasOneSolution = false
            if (allSolutions) printSolution(res)
        }
    }

    /*  Converting DLX list to Sudoku grid
        For each node, the associated value and the position in the Sudoku grid are retrieved.  */
    private fun parseBoard(answer: List<DancingNode>?): Array<IntArray> {
        val result = Array(boardSize) { IntArray(boardSize) }
        for (n in answer!!) {
            var rcNode = n
            var min = rcNode.column.name.toInt()
            var tmp = n.right
            while (tmp !== n) {
                val value = tmp.column.name.toInt()
                if (value < min) {
                    min = value
                    rcNode = tmp
                }
                tmp = tmp.right
            }
            val ans1 = rcNode.column.name.toInt()
            val ans2 = rcNode.right.column.name.toInt()
            val r = ans1 / boardSize
            val c = ans1 % boardSize
            val num = ans2 % boardSize + 1
            result[r][c] = num
        }
        return result
    }

    private fun printSolution(grid: Array<IntArray>) {
        if (needToPrint) {
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
    }

    fun getSolutionsCount(): Int {
        return solutionsCount
    }

    fun getAnswerGrid(): Array<IntArray> {
        return answerGrid
    }
}
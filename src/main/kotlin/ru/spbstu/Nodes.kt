package ru.spbstu

open class DancingNode {
    //Pointers to others nodes in 4 directions
    var left: DancingNode
    var right: DancingNode
    var up: DancingNode
    var down: DancingNode

    //Pointer to the belonging column in cover matrix
    internal lateinit var column: ColumnNode

    init {
        down = this
        up = down
        right = up
        left = right
    }

    fun linkDown(node: DancingNode): DancingNode {
        assert(column == node.column)
        node.down = down
        node.down.up = node
        node.up = this
        down = node
        return node
    }

    fun linkRight(node: DancingNode): DancingNode {
        node.right = right
        node.right.left = node
        node.left = this
        right = node
        return node
    }

    fun unlinkUpDown() {
        up.down = down
        down.up = up
    }

    fun unlinkLeftRight() {
        left.right = right
        right.left = left
    }

    fun relinkUpDown() {
        down.up = this
        up.down = down.up
    }

    fun relinkLeftRight() {
        right.left = this
        left.right = right.left
    }
}

internal class ColumnNode(n: String) : DancingNode() {
    //Count of nodes in current column and column's name
    var size: Int = 0
    var name: String

    init {
        size = 0
        name = n
        column = this
    }

    //'Removing' columns and all the rows to which nodes of the column belong to
    fun cover() {
        unlinkLeftRight()
        var i = this.down
        while (i !== this) {
            var j = i.right
            while (j !== i) {
                j.unlinkUpDown()
                j.column.size--
                j = j.right
            }
            i = i.down
        }
    }

    //Backtracking and linking again 'removed' columns and rows in case of reaching a dead end while covering
    fun uncover() {
        var i = this.up
        while (i !== this) {
            var j = i.left
            while (j !== i) {
                j.column.size++
                j.relinkUpDown()
                j = j.left
            }
            i = i.up
        }
        relinkLeftRight()
    }
}
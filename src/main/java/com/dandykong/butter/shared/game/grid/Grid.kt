package com.dandykong.butter.shared.game.grid

import com.dandykong.butter.shared.exception.ButterException

abstract class Grid<IdType>(val nrRows: Int, val nrColumns: Int) {
    var cells = Array(nrRows) { IntArray(nrColumns) { 0 } }
    var winningPlayer: Int? = null
    var filledCells: Int = 0

    fun getCell(row: Int, column: Int): Int {
        return cells[row][column]
    }

    fun isCellEmpty(row: Int, column: Int): Boolean {
        return cells[row][column] == 0
    }

    fun setCell(row: Int, column: Int, playerId: Int) {
        if (!isCellEmpty(row, column)) {
            throw ButterException("Cell not empty: [$row, $column]")
        }

        cells[row][column] = playerId
        filledCells ++
        if (checkForWin(row, column)) {
            winningPlayer = playerId
        }
    }

    fun isFull(): Boolean {
        return filledCells == nrRows * nrColumns
    }

    abstract fun checkForWin(row: Int, column: Int): Boolean

    abstract fun generateId(playerId: Int): IdType

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid<IdType>

        if (!cells.contentDeepEquals(other.cells)) return false
        if (winningPlayer != other.winningPlayer) return false
        if (filledCells != other.filledCells) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cells.contentDeepHashCode()
        result = 31 * result + (winningPlayer ?: 0)
        result = 31 * result + filledCells
        return result
    }
}


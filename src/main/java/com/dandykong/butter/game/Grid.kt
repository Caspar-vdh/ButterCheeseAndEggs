package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException

const val NR_GRID_COLUMNS = 3
const val NR_GRID_ROWS = 3
const val NR_PLAYERS = 2

class Grid {
    private lateinit var cells: Array<Array<Int>>
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
        if (playerId != PLAYER_1 && playerId != PLAYER_2) {
            throw ButterException("Invalid Player ID: $playerId")
        }
        cells[row][column] = playerId
        filledCells ++
        if (checkForWin(row, column)) {
            winningPlayer = playerId
        }
    }

    fun isFull(): Boolean {
        return filledCells == 9
    }

    private fun checkForWin(row: Int, column: Int): Boolean {
        // check row
        if (cells[row][0] == cells[row][1] && cells[row][0] == cells[row][2]) {
            return true;
        }
        //check colums
        if (cells[0][column] == cells[1][column] && cells[0][column] == cells[2][column]) {
            return true;
        }
        if (isOnTopLeftDiagonal(row, column)
            && cells[0][0] == cells[1][1]
            && cells[0][0] == cells[2][2]
        ) {
            return true
        }
        if (isOnTopRightDiagonal(row, column)
            && cells[0][2] == cells[1][1]
            && cells[0][2] == cells[2][0]
        ) {
            return true
        }
        return false
    }

    private fun isOnTopLeftDiagonal(row: Int, column: Int): Boolean {
        return row == column
    }

    private fun isOnTopRightDiagonal(row: Int, column: Int): Boolean {
        return kotlin.math.abs(row - column) == 2
                || (row == 1 && column == 1)
    }

    fun generateId(playerId: Int): Int {
        var id = 0
        for (row in 0 until NR_GRID_ROWS) {
            for (column in 0 until NR_GRID_COLUMNS) {
                val index = rowAndColumToActionId(row, column)
                if (!isCellEmpty(row, column)) {
                    val cellState =
                        if (getCell(row, column) == playerId) CellState.MINE
                        else CellState.THEIRS
                    id = id or (cellState.state shl (index * 2))
                }
            }
        }
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Grid

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


    companion object {
        fun createFromId(id: Int, playerId: Int, opponentPlayerId: Int): Grid {
            val grid = createInitial()

            for (index in 0 until NR_GRID_ROWS * NR_GRID_COLUMNS) {
                val mask = 0x3 shl(index * 2)
                val (row, column) = actionIdToRowAndColumn(index)
                val cellState = CellState.fromInt((id and mask) shr(index * 2))
                 when(cellState) {
                     CellState.EMPTY -> continue
                     CellState.MINE -> grid.setCell(row, column, playerId)
                     CellState.THEIRS -> grid.setCell(row, column, opponentPlayerId)
                 }
            }
            return grid
        }

        fun createInitial():Grid {
            val grid = Grid()
            grid.cells = createEmptyGrid()
            grid.winningPlayer = null
            grid.filledCells = 0
            return grid
        }

        private fun createEmptyGrid(): Array<Array<Int>> = arrayOf(
            Array(NR_GRID_COLUMNS) { 0 },
            Array(NR_GRID_COLUMNS) { 0 },
            Array(NR_GRID_COLUMNS) { 0 }
        )

        const val PLAYER_1 = 1
        const val PLAYER_2 = 2
    }
}


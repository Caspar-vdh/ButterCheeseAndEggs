package com.dandykong.butter.tictactoe.game.grid

import com.dandykong.butter.shared.game.CellState
import com.dandykong.butter.shared.game.grid.Grid
import com.dandykong.butter.tictactoe.state.actionIdToRowAndColumn
import com.dandykong.butter.tictactoe.state.rowAndColumToActionId
import kotlin.math.abs

const val NR_GRID_ROWS = 3
const val NR_GRID_COLUMNS = 3

class TicTacToeGrid: Grid<Int>(NR_GRID_ROWS, NR_GRID_COLUMNS) {
    override fun checkForWin(row: Int, column: Int): Boolean {
        // check row
        if (cells[row][0] == cells[row][1] && cells[row][0] == cells[row][2]) {
            return true
        }
        //check colums
        if (cells[0][column] == cells[1][column] && cells[0][column] == cells[2][column]) {
            return true
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

    override fun generateId(playerId: Int): Int {
        var id = 0
        for (row in 0 until nrRows) {
            for (column in 0 until nrColumns) {
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

    private fun isOnTopLeftDiagonal(row: Int, column: Int): Boolean {
        return row == column
    }

    private fun isOnTopRightDiagonal(row: Int, column: Int): Boolean {
        return abs(row - column) == 2
                || (row == 1 && column == 1)
    }

    companion object {
        fun createFromId(id: Int, playerId: Int, opponentPlayerId: Int): TicTacToeGrid {
            val grid = TicTacToeGrid()

            for (index in 0 until NR_GRID_ROWS * NR_GRID_COLUMNS) {
                val mask = 0x3 shl(index * 2)
                val (row, column) = actionIdToRowAndColumn(index)
                val cellState = CellState.fromInt((id and mask) shr (index * 2))
                when(cellState) {
                    CellState.EMPTY -> continue
                    CellState.MINE -> grid.setCell(row, column, playerId)
                    CellState.THEIRS -> grid.setCell(row, column, opponentPlayerId)
                }
            }
            return grid
        }
    }
}
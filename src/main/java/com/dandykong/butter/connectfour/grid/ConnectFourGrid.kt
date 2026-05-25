package com.dandykong.butter.connectfour.grid

import com.dandykong.butter.connectfour.state.StateId
import com.dandykong.butter.shared.exception.ButterException
import com.dandykong.butter.shared.game.CellState
import com.dandykong.butter.shared.game.grid.Grid

const val NR_GRID_ROWS = 6
const val NR_GRID_COLUMNS = 7

class ConnectFourGrid: Grid<StateId>(NR_GRID_ROWS, NR_GRID_COLUMNS) {

    override fun checkForWin(row: Int, column: Int): Boolean {
        val player = cells[row][column]
        if (player == 0) return false

        // (rowDir, colDir) pairs — only need 4 directions since we check both ways
        val directions = listOf(
            0 to 1,   // horizontal
            1 to 0,   // vertical
            1 to 1,   // diagonal ↘
            1 to -1   // diagonal ↙
        )

        return directions.any { (rowDir, colDir) ->
            countInDirection(row, column, rowDir, colDir, player) +
                    countInDirection(row, column, -rowDir, -colDir, player) - 1 >= 4
        }
    }

    private fun countInDirection(row: Int, column: Int, rowDir: Int, colDir: Int, player: Int): Int {
        var count = 0
        var r = row
        var c = column
        while (r in 0 until nrRows && c in 0 until nrColumns && cells[r][c] == player) {
            count++
            r += rowDir
            c += colDir
        }
        return count
    }

    override fun generateId(playerId: Int): StateId {
        val bytes = ByteArray(11)
        for (row in 0 until nrRows) {
            for (col in 0 until nrColumns) {
                val i = row * nrColumns + col
                val bitPos = i * 2
                val byteIdx = bitPos / 8
                val bitOff = bitPos % 8

                if (!isCellEmpty(row, col)) {
                    val cellState = if (cells[row][col] == playerId) CellState.MINE
                    else CellState.THEIRS
                    val value = cellState.state
                    bytes[byteIdx] = (bytes[byteIdx].toInt() or (value shl bitOff)).toByte()
                }
            }
        }
        return StateId(bytes)
    }

    fun isColumnFull(colIdx: Int): Boolean {
        return !isCellEmpty(0, colIdx)
    }

    @Suppress("unused")
    fun addToColumn(colIdx: Int, playerId: Int) {
        if (isColumnFull(colIdx)) {
            throw ButterException("Column is full: $colIdx")
        }
        for (row in nrRows - 1 downTo 0) {
            if (isCellEmpty(row, colIdx)) {
                setCell(row, colIdx, playerId)
                return
            }
        }
    }

    companion object {
        @Suppress("unused")
        fun createFromId(id: StateId, playerId: Int, opponentPlayerId: Int): ConnectFourGrid {
            val grid = ConnectFourGrid()

            for (i in 0 until NR_GRID_ROWS * NR_GRID_COLUMNS) {
                val bitPos = i * 2
                val byteIdx = bitPos / 8
                val bitOff = bitPos % 8

                val row = i / NR_GRID_COLUMNS
                val col = i % NR_GRID_COLUMNS

                val cellState = CellState.fromInt((id.bytes[byteIdx].toInt() ushr bitOff) and 0x3)
                when (cellState) {
                    CellState.EMPTY -> continue
                    CellState.MINE -> grid.setCell(row, col, playerId)
                    CellState.THEIRS -> grid.setCell(row, col, opponentPlayerId)
                }
            }
            return grid
        }
    }
}
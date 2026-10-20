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

    /**
     * A position and its left-right mirror image share one id: the smaller of the two orientations.
     */
    override fun generateId(playerId: Int): StateId {
        return minOf(generateId(playerId, mirrored = false), generateId(playerId, mirrored = true))
    }

    override fun toGridAction(stateAction: Int, playerId: Int): Int {
        return if (isMirroredInState(playerId)) mirrorColumn(stateAction) else stateAction
    }

    private fun isMirroredInState(playerId: Int): Boolean {
        return generateId(playerId, mirrored = true) < generateId(playerId, mirrored = false)
    }

    private fun mirrorColumn(colIdx: Int): Int = nrColumns - 1 - colIdx

    private fun generateId(playerId: Int, mirrored: Boolean): StateId {
        val bytes = ByteArray(StateId.NR_BYTES)
        for (row in 0 until nrRows) {
            for (col in 0 until nrColumns) {
                val i = row * nrColumns + col
                val bitPos = i * 2
                val byteIdx = bitPos / 8
                val bitOff = bitPos % 8
                val gridCol = if (mirrored) mirrorColumn(col) else col

                if (!isCellEmpty(row, gridCol)) {
                    val cellState = if (cells[row][gridCol] == playerId) CellState.MINE
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

            for (row in 0 until NR_GRID_ROWS) {
                for (col in 0 until NR_GRID_COLUMNS) {
                    when (cellStateFromId(id, row, col)) {
                        CellState.EMPTY -> continue
                        CellState.MINE -> grid.setCell(row, col, playerId)
                        CellState.THEIRS -> grid.setCell(row, col, opponentPlayerId)
                    }
                }
            }
            return grid
        }

        fun isColumnFull(id: StateId, colIdx: Int): Boolean {
            return cellStateFromId(id, 0, colIdx) != CellState.EMPTY
        }

        private fun cellStateFromId(id: StateId, row: Int, col: Int): CellState {
            val bitPos = (row * NR_GRID_COLUMNS + col) * 2
            val byteIdx = bitPos / 8
            val bitOff = bitPos % 8
            return CellState.fromInt((id.bytes[byteIdx].toInt() ushr bitOff) and 0x3)
        }
    }
}
package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException

const val NR_GRID_COLUMNS = 3
const val NR_GRID_ROWS = 3
const val NR_PLAYERS = 2

class Grid {
    private lateinit var cells: Array<Array<String?>>
    val playerIds: Array<String?> = arrayOfNulls(NR_PLAYERS)
    var winningPlayer: String? = null
    var filledCells: Int = 0

    private fun createEmptyGrid(): Array<Array<String?>> = arrayOf(
            arrayOfNulls(NR_GRID_COLUMNS),
            arrayOfNulls(NR_GRID_COLUMNS),
            arrayOfNulls(NR_GRID_COLUMNS)
    )

    fun startGame(player1: String, player2: String) {
        cells = createEmptyGrid()
        playerIds[0] = player1
        playerIds[1] = player2
        winningPlayer = null
        filledCells = 0
    }

    fun getCell(row: Int, column: Int): String? {
        return cells[row][column]
    }

    fun isCellEmpty(row: Int, column: Int): Boolean {
        return cells[row][column] == null
    }

    fun setCell(row: Int, column: Int, playerId: String) {
        if (!playerIds.contains(playerId)) {
            throw ButterException("Unknown PlayerId: $playerId")
        }
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
}

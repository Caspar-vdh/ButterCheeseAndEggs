package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException

const val NR_GRID_COLUMNS = 3
const val NR_GRID_ROWS = 3
const val NR_PLAYERS = 2

class Grid {
    private lateinit var cells: Array<Array<String?>>
    private val playerIds: Array<String?> = arrayOfNulls(NR_PLAYERS)

    private fun createEmptyGrid(): Array<Array<String?>> = arrayOf(
            arrayOfNulls(NR_GRID_COLUMNS),
            arrayOfNulls(NR_GRID_COLUMNS),
            arrayOfNulls(NR_GRID_COLUMNS)
    )

    fun startGame(player1: String, player2: String) {
        cells = createEmptyGrid()
        playerIds[0] = player1
        playerIds[1] = player2
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
    }
}

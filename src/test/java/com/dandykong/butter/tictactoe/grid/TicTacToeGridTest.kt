package com.dandykong.butter.tictactoe.grid

import com.dandykong.butter.shared.exception.ButterException
import com.dandykong.butter.shared.game.CellState
import com.dandykong.butter.shared.player.Player
import com.dandykong.butter.tictactoe.state.rowAndColumToActionId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TicTacToeGridTest {

    @Test
    fun isCellEmpty() {
        val grid = TicTacToeGrid()
        val row = Random.nextInt(NR_GRID_COLUMNS)
        val column = Random.nextInt(NR_GRID_ROWS)

        assertTrue(grid.isCellEmpty(row, column))
    }

    @Test
    fun setCell() {
        val grid = TicTacToeGrid()
        val row = Random.nextInt(NR_GRID_COLUMNS)
        val column = Random.nextInt(NR_GRID_ROWS)

        grid.setCell(row, column, Player.PLAYER_1)

        // Setting the same cell for a second time results in an exception
        try {
            grid.setCell(row, column, Player.PLAYER_2)
            fail<Nothing>("Expected exception not thrown")
        } catch (_: ButterException) {
        }
    }

    @Test
    fun testGenerateId() {
        val grid = TicTacToeGrid()
        val player1 = Player.PLAYER_1
        val player2 = Player.PLAYER_2
        val row1 = Random.nextInt(NR_GRID_ROWS)
        val column1 = Random.nextInt(NR_GRID_COLUMNS)
        var row2 = Random.nextInt(NR_GRID_ROWS)
        var column2 = Random.nextInt(NR_GRID_COLUMNS)
        while (row1 == row2 && column1 == column2) {
            row2 = Random.nextInt(NR_GRID_ROWS)
            column2 = Random.nextInt(NR_GRID_COLUMNS)
        }

        grid.setCell(row1, column1, player1)
        grid.setCell(row2, column2, player2)

        val gridStateId = grid.generateId(player1)

        val cellState1 = (gridStateId shr (rowAndColumToActionId(row1, column1) * 2)) and 0x03
        assertEquals(cellState1, CellState.MINE.state)
        val cellState2 = (gridStateId shr (rowAndColumToActionId(row2, column2) * 2)) and 0x03
        assertEquals(cellState2, CellState.THEIRS.state)

        val generatedGrid = TicTacToeGrid.createFromId(gridStateId, player1, player2)
        assertEquals(grid, generatedGrid)
    }
}
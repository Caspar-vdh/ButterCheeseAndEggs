package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.game.grid.NR_GRID_ROWS
import com.dandykong.training.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class GridTest {

    @Test
    fun isCellEmpty() {
        val grid = Grid.createInitial()
        val row = Random.nextInt(NR_GRID_COLUMNS)
        val column = Random.nextInt(NR_GRID_ROWS)

        assertTrue(grid.isCellEmpty(row, column))
    }

    @Test
    fun setCell() {
        val grid = Grid.createInitial()
        val row = Random.nextInt(NR_GRID_COLUMNS)
        val column = Random.nextInt(NR_GRID_ROWS)

        // Setting a cell with an unknown player results in an exception
        try {
            grid.setCell(row, column, 3)
            fail<Nothing>("Expected exception not thrown")
        } catch (ignored: ButterException) {
        }

        grid.setCell(row, column, Player.PLAYER_1)

        // Setting the same cell for a second time results in an exception
        try {
            grid.setCell(row, column, Player.PLAYER_2)
            fail<Nothing>("Expected exception not thrown")
        } catch (ignored: ButterException) {
        }
    }

    @Test
    fun testGenerateId() {
        val grid = Grid.createInitial()
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

        val generatedGrid = Grid.createFromId(gridStateId, player1, player2)
        assertEquals(grid, generatedGrid)
    }
}

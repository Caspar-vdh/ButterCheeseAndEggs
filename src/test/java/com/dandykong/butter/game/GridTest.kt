package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

private const val PLAYER_1 = "PLAYER 1"
private const val PLAYER_2 = "PLAYER 2"

internal class GridTest {

    @org.junit.jupiter.api.Test
    fun isCellEmpty() {
        val grid = Grid()
        val row = Random.nextInt(NR_GRID_COLUMNS)
        val column = Random.nextInt(NR_GRID_ROWS)

        // checking whether a cell is empty should result in an UninitializedPropertyAccessException
        try {
            grid.isCellEmpty(row, column)
            fail<Nothing>("expected exception not thrown")
        } catch (ignored: UninitializedPropertyAccessException) {
        }

        grid.startGame(PLAYER_1, PLAYER_2)
        assertTrue(grid.isCellEmpty(row, column))
    }

    @org.junit.jupiter.api.Test
    fun setCell() {
        val grid = Grid()
        val row = Random.nextInt(NR_GRID_COLUMNS)
        val column = Random.nextInt(NR_GRID_ROWS)

        grid.startGame(PLAYER_1, PLAYER_2)

        // Setting a cell with an unknown player results in an exception
        try {
            grid.setCell(row, column, "PLAYER 3")
            fail<Nothing>("Expected exception not thrown")
        } catch (ignored: ButterException) {
        }

        grid.setCell(row, column, PLAYER_1)

        // Setting the same cell for a second time results in an exception
        try {
            grid.setCell(row, column, PLAYER_2)
            fail<Nothing>("Expected exception not thrown")
        } catch (ignored: ButterException) {
        }
    }
}

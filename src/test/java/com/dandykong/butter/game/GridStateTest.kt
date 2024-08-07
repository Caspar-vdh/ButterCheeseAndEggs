package com.dandykong.butter.game

import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.game.grid.NR_GRID_ROWS
import com.dandykong.training.basics.INITIAL_WEIGHT
import com.dandykong.training.player.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

internal class GridStateTest {

    @Test
    fun testRowAndColumToActionId() {
        assertEquals(0, rowAndColumToActionId(0, 0))
        assertEquals(1, rowAndColumToActionId(0, 1))
        assertEquals(2, rowAndColumToActionId(0, 2))
        assertEquals(3, rowAndColumToActionId(1, 0))
        assertEquals(4, rowAndColumToActionId(1, 1))
        assertEquals(5, rowAndColumToActionId(1, 2))
        assertEquals(6, rowAndColumToActionId(2, 0))
        assertEquals(7, rowAndColumToActionId(2, 1))
        assertEquals(8, rowAndColumToActionId(2, 2))
    }

    @Test
    fun testActionIdToRowAndColumn() {
        assertEquals(Pair(0, 0), actionIdToRowAndColumn(0))
        assertEquals(Pair(0, 1), actionIdToRowAndColumn(1))
        assertEquals(Pair(0, 2), actionIdToRowAndColumn(2))
        assertEquals(Pair(1, 0), actionIdToRowAndColumn(3))
        assertEquals(Pair(1, 1), actionIdToRowAndColumn(4))
        assertEquals(Pair(1, 2), actionIdToRowAndColumn(5))
        assertEquals(Pair(2, 0), actionIdToRowAndColumn(6))
        assertEquals(Pair(2, 1), actionIdToRowAndColumn(7))
        assertEquals(Pair(2, 2), actionIdToRowAndColumn(8))
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testCreateGridStateFromGid() {
        val grid = Grid.createInitial()
        val row1 = Random.nextInt(NR_GRID_ROWS)
        val column1 = Random.nextInt(NR_GRID_COLUMNS)
        var row2 = Random.nextInt(NR_GRID_ROWS)
        var column2 = Random.nextInt(NR_GRID_COLUMNS)
        while (row1 == row2 && column1 == column2) {
            row2 = Random.nextInt(NR_GRID_ROWS)
            column2 = Random.nextInt(NR_GRID_COLUMNS)
        }

        grid.setCell(row1, column1, Player.PLAYER_1)
        grid.setCell(row2, column2, Player.PLAYER_2)

        val id = 123
        val gridState = GridState.createNewFromGrid(grid, id)
        val weights: UByteArray = gridState.weights
        for (i in 0 until 9) {
            when (i) {
                rowAndColumToActionId(row1, column1) -> assertEquals(0.toUByte(), weights[i])
                rowAndColumToActionId(row2, column2) -> assertEquals(0.toUByte(), weights[i])
                else -> assertEquals(INITIAL_WEIGHT, weights[i])
            }
        }
        assertEquals(id, gridState.id)
    }
}

package com.dandykong.butter.game

import com.dandykong.training.basics.INITIAL_WEIGHT
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
        val grid = Grid()
        val player1 = "PLAYER_1"
        val player2 = "PLAYER_2"
        val row1 = Random.nextInt(NR_GRID_ROWS)
        val column1 = Random.nextInt(NR_GRID_COLUMNS)
        var row2 = Random.nextInt(NR_GRID_ROWS)
        var column2 = Random.nextInt(NR_GRID_COLUMNS)
        while (row1 == row2 && column1 == column2) {
            row2 = Random.nextInt(NR_GRID_ROWS)
            column2 = Random.nextInt(NR_GRID_COLUMNS)
        }

        grid.startGame(player1, player2)
        grid.setCell(row1, column1, player1)
        grid.setCell(row2, column2, player2)

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

    @Test
    fun testGenerateId() {
        val grid = Grid()
        val player1 = "PLAYER_1"
        val player2 = "PLAYER_2"
        val row1 = Random.nextInt(NR_GRID_ROWS)
        val column1 = Random.nextInt(NR_GRID_COLUMNS)
        var row2 = Random.nextInt(NR_GRID_ROWS)
        var column2 = Random.nextInt(NR_GRID_COLUMNS)
        while (row1 == row2 && column1 == column2) {
            row2 = Random.nextInt(NR_GRID_ROWS)
            column2 = Random.nextInt(NR_GRID_COLUMNS)
        }

        grid.startGame(player1, player2)
        grid.setCell(row1, column1, player1)
        grid.setCell(row2, column2, player2)

        val gridStateId = GridState.generateId(grid, player1)

        val cellState1 = (gridStateId shr (rowAndColumToActionId(row1, column1) * 2)) and 0x03
        assertEquals(cellState1, CellState.MINE.state)
        val cellState2 = (gridStateId shr (rowAndColumToActionId(row2, column2) * 2)) and 0x03
        assertEquals(cellState2, CellState.THEIRS.state)
    }
}

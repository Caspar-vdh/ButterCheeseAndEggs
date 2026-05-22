package com.dandykong.butter.tictactoe.game

import com.dandykong.butter.tictactoe.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_ROWS
import com.dandykong.butter.tictactoe.game.grid.TicTacToeGrid
import com.dandykong.butter.tictactoe.state.TicTacToeGridState
import com.dandykong.butter.tictactoe.state.actionIdToRowAndColumn
import com.dandykong.butter.tictactoe.state.rowAndColumToActionId
import com.dandykong.butter.shared.state.INITIAL_WEIGHT
import com.dandykong.butter.shared.player.Player
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TicTactToeGridStateTest {

    @Test
    fun testRowAndColumToActionId() {
        Assertions.assertEquals(0, rowAndColumToActionId(0, 0))
        Assertions.assertEquals(1, rowAndColumToActionId(0, 1))
        Assertions.assertEquals(2, rowAndColumToActionId(0, 2))
        Assertions.assertEquals(3, rowAndColumToActionId(1, 0))
        Assertions.assertEquals(4, rowAndColumToActionId(1, 1))
        Assertions.assertEquals(5, rowAndColumToActionId(1, 2))
        Assertions.assertEquals(6, rowAndColumToActionId(2, 0))
        Assertions.assertEquals(7, rowAndColumToActionId(2, 1))
        Assertions.assertEquals(8, rowAndColumToActionId(2, 2))
    }

    @Test
    fun testActionIdToRowAndColumn() {
        Assertions.assertEquals(Pair(0, 0), actionIdToRowAndColumn(0))
        Assertions.assertEquals(Pair(0, 1), actionIdToRowAndColumn(1))
        Assertions.assertEquals(Pair(0, 2), actionIdToRowAndColumn(2))
        Assertions.assertEquals(Pair(1, 0), actionIdToRowAndColumn(3))
        Assertions.assertEquals(Pair(1, 1), actionIdToRowAndColumn(4))
        Assertions.assertEquals(Pair(1, 2), actionIdToRowAndColumn(5))
        Assertions.assertEquals(Pair(2, 0), actionIdToRowAndColumn(6))
        Assertions.assertEquals(Pair(2, 1), actionIdToRowAndColumn(7))
        Assertions.assertEquals(Pair(2, 2), actionIdToRowAndColumn(8))
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testCreateGridStateFromGid() {
        val grid = TicTacToeGrid()
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
        val ticTacToeGridState = TicTacToeGridState.createNewFromGrid(grid, id)
        val weights: UByteArray = ticTacToeGridState.weights
        for (i in 0 until 9) {
            when (i) {
                rowAndColumToActionId(row1, column1) -> Assertions.assertEquals(0.toUByte(), weights[i])
                rowAndColumToActionId(row2, column2) -> Assertions.assertEquals(0.toUByte(), weights[i])
                else -> Assertions.assertEquals(INITIAL_WEIGHT, weights[i])
            }
        }
        Assertions.assertEquals(id, ticTacToeGridState.id)
    }
}
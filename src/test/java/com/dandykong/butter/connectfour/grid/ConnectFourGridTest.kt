package com.dandykong.butter.connectfour.grid

import com.dandykong.butter.connectfour.state.ConnectFourGridState
import com.dandykong.butter.shared.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalUnsignedTypes::class)
internal class ConnectFourGridTest {
    @Test
    fun mirroredPositionsShouldHaveSameId() {
        val (grid, mirroredGrid) = createGridAndMirror()

        assertEquals(grid.generateId(Player.PLAYER_1), mirroredGrid.generateId(Player.PLAYER_1))
        assertEquals(grid.generateId(Player.PLAYER_2), mirroredGrid.generateId(Player.PLAYER_2))
    }

    @Test
    fun differentPositionsShouldHaveDifferentIds() {
        val grid = ConnectFourGrid()
        grid.addToColumn(0, Player.PLAYER_1)
        val otherGrid = ConnectFourGrid()
        otherGrid.addToColumn(1, Player.PLAYER_1)

        assertNotEquals(grid.generateId(Player.PLAYER_1), otherGrid.generateId(Player.PLAYER_1))
    }

    @Test
    fun stateActionShouldMapToMirroredColumnsInMirroredPositions() {
        val (grid, mirroredGrid) = createGridAndMirror()

        for (action in 0 until NR_GRID_COLUMNS) {
            assertEquals(
                NR_GRID_COLUMNS - 1 - grid.toGridAction(action, Player.PLAYER_2),
                mirroredGrid.toGridAction(action, Player.PLAYER_2)
            )
        }
    }

    @Test
    fun stateActionShouldBeGridActionInSymmetricPosition() {
        val grid = ConnectFourGrid()
        grid.addToColumn(3, Player.PLAYER_1)

        for (action in 0 until NR_GRID_COLUMNS) {
            assertEquals(action, grid.toGridAction(action, Player.PLAYER_2))
        }
    }

    @Test
    fun stateWeightsShouldOnlyAllowNonFullColumns() {
        val (grid, mirroredGrid) = createGridAndMirror()
        repeat(NR_GRID_ROWS) { grid.addToColumn(1, Player.PLAYER_1 + it % 2) }
        repeat(NR_GRID_ROWS) { mirroredGrid.addToColumn(5, Player.PLAYER_1 + it % 2) }

        for (g in listOf(grid, mirroredGrid)) {
            val state = ConnectFourGridState.createNewFromId(g.generateId(Player.PLAYER_2))
            for (action in 0 until NR_GRID_COLUMNS) {
                assertEquals(g.isColumnFull(g.toGridAction(action, Player.PLAYER_2)), state.weights[action] == 0u.toUByte())
            }
        }
    }

    @Test
    fun gridCreatedFromIdShouldHaveSameId() {
        val (grid, _) = createGridAndMirror()
        val id = grid.generateId(Player.PLAYER_1)

        val recreated = ConnectFourGrid.createFromId(id, Player.PLAYER_1, Player.PLAYER_2)

        assertEquals(id, recreated.generateId(Player.PLAYER_1))
    }

    /**
     * An asymmetric position and its left-right mirror image
     */
    private fun createGridAndMirror(): Pair<ConnectFourGrid, ConnectFourGrid> {
        val grid = ConnectFourGrid()
        val mirroredGrid = ConnectFourGrid()
        for ((column, playerId) in listOf(0 to Player.PLAYER_1, 2 to Player.PLAYER_2, 0 to Player.PLAYER_1)) {
            grid.addToColumn(column, playerId)
            mirroredGrid.addToColumn(NR_GRID_COLUMNS - 1 - column, playerId)
        }
        return Pair(grid, mirroredGrid)
    }
}

package com.dandykong.butter.connectfour.state

import com.dandykong.butter.connectfour.grid.ConnectFourGrid
import com.dandykong.butter.connectfour.grid.NR_GRID_COLUMNS
import com.dandykong.butter.shared.player.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream

@OptIn(ExperimentalUnsignedTypes::class)
internal class ConnectFourStateStoreTest {
    @Test
    fun persistedStoreShouldReloadWithSameStatesAndWeights() {
        val grid = ConnectFourGrid()
        val emptyGridState = ConnectFourGridState.createNewFromGrid(grid, grid.generateId(Player.PLAYER_1))
        grid.addToColumn(3, Player.PLAYER_1)
        grid.addToColumn(3, Player.PLAYER_2)
        grid.addToColumn(6, Player.PLAYER_1)
        val laterState = ConnectFourGridState.createNewFromGrid(grid, grid.generateId(Player.PLAYER_2))
        laterState.weights[2] = 200u
        laterState.weights[6] = 1u

        val store = createStore(null)
        store.addState(emptyGridState)
        store.addState(laterState)
        val bytes = ByteArrayOutputStream()
        DataOutputStream(bytes).use { store.persistStore(it) }

        val reloaded = createStore(DataInputStream(ByteArrayInputStream(bytes.toByteArray())))

        for (state in listOf(emptyGridState, laterState)) {
            val reloadedState = reloaded.getStateForId(state.id)
            assertNotNull(reloadedState)
            assertArrayEquals(state.weights.toByteArray(), reloadedState!!.weights.toByteArray())
        }
    }

    private fun createStore(inputStream: DataInputStream?) =
        ConnectFourStateStore(inputStream, NR_GRID_COLUMNS, ConnectFourGridStateFactory())
}

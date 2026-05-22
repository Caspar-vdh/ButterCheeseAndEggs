package com.dandykong.butter.connectfour.state
import com.dandykong.butter.connectfour.grid.ConnectFourGrid
import com.dandykong.butter.connectfour.grid.NR_GRID_COLUMNS
import com.dandykong.butter.shared.state.INITIAL_WEIGHT
import com.dandykong.butter.shared.state.State


@OptIn(ExperimentalUnsignedTypes::class)
class ConnectFourGridState(id: StateId, weights: UByteArray): State<StateId>(id, weights)  {
    companion object {

        @Suppress("unused")
        fun createNewFromGrid(grid: ConnectFourGrid, id: StateId): ConnectFourGridState {
            val weights = createWeights(grid)
            return ConnectFourGridState(id, weights)
        }

        private fun createWeights(grid: ConnectFourGrid): UByteArray {
            val weights = UByteArray(NR_GRID_COLUMNS) {0U}
            for (column in 0 until NR_GRID_COLUMNS) {
                if (!grid.isColumnFull(column)) {
                    weights[column] = INITIAL_WEIGHT
                }
            }
            return weights
        }
    }
}
package com.dandykong.butter.connectfour.state
import com.dandykong.butter.connectfour.grid.ConnectFourGrid
import com.dandykong.butter.connectfour.grid.NR_GRID_COLUMNS
import com.dandykong.butter.shared.state.INITIAL_WEIGHT
import com.dandykong.butter.shared.state.State


@OptIn(ExperimentalUnsignedTypes::class)
class ConnectFourGridState(id: StateId, weights: UByteArray): State<StateId>(id, weights)  {
    companion object {

        /**
         * Weights follow the orientation of the id, which may be the mirror image of the grid it was generated from
         */
        fun createNewFromId(id: StateId): ConnectFourGridState {
            return ConnectFourGridState(id, createWeights(id))
        }

        private fun createWeights(id: StateId): UByteArray {
            val weights = UByteArray(NR_GRID_COLUMNS) {0U}
            for (column in 0 until NR_GRID_COLUMNS) {
                if (!ConnectFourGrid.isColumnFull(id, column)) {
                    weights[column] = INITIAL_WEIGHT
                }
            }
            return weights
        }
    }
}

package com.dandykong.butter.tictactoe.state

import com.dandykong.butter.tictactoe.grid.NR_GRID_COLUMNS
import com.dandykong.butter.tictactoe.grid.TicTacToeGrid
import com.dandykong.butter.shared.state.INITIAL_WEIGHT
import com.dandykong.butter.shared.state.State

const val MAX_NR_ACTIONS = NR_GRID_COLUMNS * NR_GRID_COLUMNS

@OptIn(ExperimentalUnsignedTypes::class)
class TicTacToeGridState(id: Int, weights: UByteArray): State<Int>(id, weights) {
    companion object {
        fun createNewFromGrid(grid: TicTacToeGrid, id: Int): TicTacToeGridState {
            val weights = createWeights(grid)
            return TicTacToeGridState(id, weights)
        }

        private fun createWeights(grid: TicTacToeGrid): UByteArray {
            val weights = UByteArray(MAX_NR_ACTIONS)
            for (row in 0 until NR_GRID_COLUMNS) {
                for (column in 0 until NR_GRID_COLUMNS) {
                    val index = rowAndColumToActionId(row, column)
                    if (grid.isCellEmpty(row, column)) {
                        weights[index] = INITIAL_WEIGHT
                    }
                }
            }
            return weights
        }
    }
}

fun rowAndColumToActionId(row: Int, column: Int): Int {
    return row * NR_GRID_COLUMNS + column
}

fun actionIdToRowAndColumn(actionId: Int): Pair<Int, Int> {
    return Pair(actionId / NR_GRID_COLUMNS, actionId.mod(NR_GRID_COLUMNS))
}

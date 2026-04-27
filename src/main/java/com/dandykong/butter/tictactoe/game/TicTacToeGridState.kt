package com.dandykong.butter.tictactoe.game

import com.dandykong.butter.shared.game.createWeights
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_ROWS
import com.dandykong.butter.tictactoe.game.grid.TicTacToeGrid
import com.dandykong.training.basics.INITIAL_WEIGHT
import com.dandykong.training.basics.State

@OptIn(ExperimentalUnsignedTypes::class)
class TicTacToeGridState(id: Int, weights: UByteArray): State(id, weights) {
    companion object {
        fun createNewFromGrid(grid: TicTacToeGrid, id: Int): TicTacToeGridState {
            val weights = createWeights(grid, NR_GRID_ROWS, NR_GRID_COLUMNS, INITIAL_WEIGHT)
            return TicTacToeGridState(id, weights)
        }
    }
}

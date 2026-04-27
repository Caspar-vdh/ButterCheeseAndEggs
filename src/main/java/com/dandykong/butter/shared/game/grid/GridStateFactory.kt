package com.dandykong.butter.shared.game.grid

import com.dandykong.butter.tictactoe.game.TicTacToeGridState
import com.dandykong.training.basics.StateFactory

class GridStateFactory: StateFactory<TicTacToeGridState> {
    @ExperimentalUnsignedTypes
    override fun createNew(id: Int, weights: UByteArray): TicTacToeGridState {
        return TicTacToeGridState(id, weights)
    }

}
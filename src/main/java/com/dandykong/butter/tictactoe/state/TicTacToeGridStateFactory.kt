package com.dandykong.butter.tictactoe.state

import com.dandykong.butter.shared.state.StateFactory

class TicTacToeGridStateFactory: StateFactory<TicTacToeGridState, Int> {
    @ExperimentalUnsignedTypes
    override fun createNew(id: Int, weights: UByteArray): TicTacToeGridState {
        return TicTacToeGridState(id, weights)
    }

}
package com.dandykong.butter.game.grid

import com.dandykong.butter.game.GridState
import com.dandykong.training.basics.StateFactory

class GridStateFactory: StateFactory<GridState> {
    @ExperimentalUnsignedTypes
    override fun createNew(id: Int, weights: UByteArray): GridState {
        return GridState(id, weights)
    }

}
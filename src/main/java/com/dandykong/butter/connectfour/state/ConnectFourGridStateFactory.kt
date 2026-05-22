package com.dandykong.butter.connectfour.state

import com.dandykong.butter.shared.state.StateFactory

@OptIn(ExperimentalUnsignedTypes::class)
class ConnectFourGridStateFactory: StateFactory<ConnectFourGridState, StateId> {
    override fun createNew(
        id: StateId,
        weights: UByteArray
    ): ConnectFourGridState {
        return ConnectFourGridState(id, weights)
    }
}
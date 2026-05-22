package com.dandykong.butter.shared.state

interface StateFactory<StateType, StateIdType> where StateType: State<StateIdType> {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun createNew(id: StateIdType, weights: UByteArray): StateType
}
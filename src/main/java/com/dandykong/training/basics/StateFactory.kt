package com.dandykong.training.basics

interface StateFactory<S> where S: State {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun createNew(id: Int, weights: UByteArray): S
}
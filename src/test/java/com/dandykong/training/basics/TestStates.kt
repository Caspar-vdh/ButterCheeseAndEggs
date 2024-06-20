package com.dandykong.training.basics

@OptIn(ExperimentalUnsignedTypes::class)
class TestState(id: Int, weights: UByteArray) : State(id, weights) {
}

@OptIn(ExperimentalUnsignedTypes::class)
class TestState1(id: Int, weights: UByteArray) : State(id, weights)

@OptIn(ExperimentalUnsignedTypes::class)
class TestState2(id: Int, weights: UByteArray) : State(id, weights)

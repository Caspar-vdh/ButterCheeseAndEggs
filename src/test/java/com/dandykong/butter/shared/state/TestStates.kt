@file:OptIn(ExperimentalUnsignedTypes::class)

package com.dandykong.butter.shared.state

class TestState(id: Int, weights: UByteArray) : State<Int>(id, weights)

class TestState1(id: Int, weights: UByteArray) : State<Int>(id, weights)

class TestState2(id: Int, weights: UByteArray) : State<Int>(id, weights)

class TestStateFactory: StateFactory<TestState, Int> {
    override fun createNew(id: Int, weights: UByteArray): TestState {
        return TestState(id, weights)
    }
}
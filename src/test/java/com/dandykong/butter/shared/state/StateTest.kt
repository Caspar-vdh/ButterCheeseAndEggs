package com.dandykong.butter.shared.state

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@OptIn(ExperimentalUnsignedTypes::class)
class StateTest {
    @Test
    fun statesAreEqualWhenIdIsEqual() {
        val id = 123
        val state1 = TestState1(id, ubyteArrayOf())
        val state2 = TestState1(id, ubyteArrayOf())
        Assertions.assertTrue(state1 == state2)
    }

    @Test
    fun statesArNotEqualWhenIdIsNotEqual() {
        val id1 = 123
        val id2 = 456
        val state1 = TestState1(id1, ubyteArrayOf())
        val state2 = TestState1(id2, ubyteArrayOf())
        Assertions.assertFalse(state1 == state2)
    }

    @Test
    fun statesAreNotEqualWhenClassIsNotEqual() {
        val id = 123
        val state1 = TestState1(id, ubyteArrayOf())
        val state2 = TestState2(id, ubyteArrayOf())
        Assertions.assertFalse(state1.equals(state2))
    }
}
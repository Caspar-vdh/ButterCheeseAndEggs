@file:OptIn(ExperimentalUnsignedTypes::class)

package com.dandykong.training.basics

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalUnsignedTypes::class)
internal class StateStoreTest {
    @Test
    fun convertFromUnsignedToSignedAndBack() {
        val unsignedArray = ubyteArrayOf(255u, 127u, 0u)
        val signedArray = unsignedArray.toByteArray()

        assertEquals(unsignedArray[0], signedArray.toUByteArray()[0])
        assertEquals(unsignedArray[1], signedArray.toUByteArray()[1])
        assertEquals(unsignedArray[2], signedArray.toUByteArray()[2])

        val unsignedByte: UByte = 133u
        val asInt = unsignedByte.toInt()
        assertEquals(unsignedByte, asInt.toUByte())
    }

    @Test
    fun addStateToStore() {
        val stateStore = StateStore(null, 3, TestStateFactory())

        val testState = TestState(123, ubyteArrayOf())
        assertNull(stateStore.getStateForId(testState.id))
        stateStore.addState(testState)
        assertEquals(testState, stateStore.getStateForId(123))
    }
}

package com.dandykong.butter.shared.state

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.DataInputStream
import java.io.DataOutputStream

@OptIn(ExperimentalUnsignedTypes::class)
internal class StateStoreTest {
    @Test
    fun convertFromUnsignedToSignedAndBack() {
        val unsignedArray = ubyteArrayOf(255u, 127u, 0u)
        val signedArray = unsignedArray.toByteArray()

        Assertions.assertEquals(unsignedArray[0], signedArray.toUByteArray()[0])
        Assertions.assertEquals(unsignedArray[1], signedArray.toUByteArray()[1])
        Assertions.assertEquals(unsignedArray[2], signedArray.toUByteArray()[2])

        val unsignedByte: UByte = 133u
        val asInt = unsignedByte.toInt()
        Assertions.assertEquals(unsignedByte, asInt.toUByte())
    }

    @Test
    fun addStateToStore() {
        val stateStore = object : StateStore<TestState, Int>(null, 3, TestStateFactory()) {
            override fun writeId(stream: DataOutputStream, id: Int) {
            }

            override fun readId(stream: DataInputStream): Int {
                return 0
            }
        }

        val testState = TestState(123, ubyteArrayOf())
        Assertions.assertNull(stateStore.getStateForId(testState.id))
        stateStore.addState(testState)
        Assertions.assertEquals(testState, stateStore.getStateForId(123))
    }
}
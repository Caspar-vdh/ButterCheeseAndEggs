package com.dandykong.training.rewardstrategies

import com.dandykong.training.basics.INITIAL_WEIGHT
import com.dandykong.training.basics.MAX_WEIGHT
import com.dandykong.training.basics.State
import com.dandykong.training.basics.TestState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

const val NR_TEST_ACTIONS: Int = 9

@OptIn(ExperimentalUnsignedTypes::class)
internal class PositiveRewardStrategy1Test {
    @Test
    fun shouldUpdateWeightsForTrainingSession() {
        val testState1 = TestState(1, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        val testState2 = TestState(2, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        val testState4 = TestState(4, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        val testState6 = TestState(6, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        val testState9 = TestState(9, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        val selectedWeights = mutableMapOf<State, Int>(
            Pair(testState1, 1),
            Pair(testState2, 3),
            Pair(testState4, 5),
            Pair(testState6, 7),
            Pair(testState9, 8)
        )

        PositiveRewardStrategy1().updateWeights(selectedWeights)

        assertEquals((INITIAL_WEIGHT + 1u).toUByte(), testState1.weights[1])
        assertEquals((INITIAL_WEIGHT + 2u).toUByte(), testState2.weights[3])
        assertEquals((INITIAL_WEIGHT + 3u).toUByte(), testState4.weights[5])
        assertEquals((INITIAL_WEIGHT + 4u).toUByte(), testState6.weights[7])
        assertEquals((INITIAL_WEIGHT + 5u).toUByte(), testState9.weights[8])
    }

    @Test
    fun shouldNotUpdateWeightIfItAlreadyReachedItsMaximumValue() {
        val testState2 = TestState(2, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        testState2.weights[3] = (MAX_WEIGHT - 2u).toUByte()
        val testState6 = TestState(6, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT })
        testState6.weights[7] = MAX_WEIGHT

        val selectedWeights = mutableMapOf<State, Int>(
            Pair(testState2, 3),
            Pair(testState6, 7),
        )

        PositiveRewardStrategy1().updateWeights(selectedWeights)

        assertEquals(MAX_WEIGHT, testState2.weights[3])
        assertEquals(MAX_WEIGHT, testState6.weights[7])
    }
}

package com.dandykong.butter.shared.player

import com.dandykong.butter.shared.actionselectionstrategies.SelectFirstNonZeroStrategy
import com.dandykong.butter.shared.rewardstrategies.PositiveRewardStrategy1
import com.dandykong.butter.shared.state.INITIAL_WEIGHT
import com.dandykong.butter.shared.state.TestState
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

private const val NR_TEST_ACTIONS: Int = 9

@OptIn(ExperimentalUnsignedTypes::class)
internal class CPUPlayerTest {
    @Test
    fun lastPlayedStateShouldReceiveLargestReward() {
        val player = CPUPlayer<Int, TestState>(Player.PLAYER_1, SelectFirstNonZeroStrategy(null))
        // Played in descending id order, the reverse of how a HashMap would iterate them
        val states = listOf(9, 6, 4, 2, 1).map { TestState(it, UByteArray(NR_TEST_ACTIONS) { INITIAL_WEIGHT }) }
        states.forEach { player.nextAction(it) }

        PositiveRewardStrategy1<Int, TestState>().updateWeights(player.selectedActions)

        assertEquals(listOf(9, 6, 4, 2, 1), player.selectedActions.keys.map { it.id })
        assertEquals((INITIAL_WEIGHT + 1u).toUByte(), states[0].weights[0])
        assertEquals((INITIAL_WEIGHT + 5u).toUByte(), states[4].weights[0])
    }
}

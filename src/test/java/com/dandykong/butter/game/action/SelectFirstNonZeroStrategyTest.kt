package com.dandykong.butter.game.action

import com.dandykong.training.actionselectionstrategies.SelectFirstNonZeroStrategy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class SelectFirstNonZeroStrategyTest {
    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testSelectAction() {
        val weights = UByteArray(9) { 0U }
        val nonZeroIndex = Random.nextInt(weights.size)
        weights[nonZeroIndex] = 127.toUByte()

        assertEquals(nonZeroIndex, SelectFirstNonZeroStrategy().selectAction(weights))
    }
}

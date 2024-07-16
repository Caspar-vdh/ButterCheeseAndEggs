package com.dandykong.training.actionselectionstrategies

import com.dandykong.logger.LOG
import kotlin.random.Random

class RandomSelectionStrategy: ActionSelectionStrategy {
    @ExperimentalUnsignedTypes
    override fun selectAction(weights: UByteArray): Int {
        LOG.info("Using ${this.javaClass.simpleName}")
        val validActions = mutableListOf<Int>()
        for (i in weights.indices) {
            if (weights[i] != 0u.toUByte()) {
                validActions.add(i)
            }
        }
        return validActions[Random.nextInt(validActions.size)]
    }
}

package com.dandykong.butter.shared.actionselectionstrategies

import com.dandykong.logger.ButterLogger
import kotlin.random.Random

class RandomSelectionStrategy(log: ButterLogger?): ActionSelectionStrategy(log) {

    @ExperimentalUnsignedTypes
    override fun selectAction(weights: UByteArray): Int {
        log?.info("Using ${this.javaClass.simpleName}")
        val validActions = mutableListOf<Int>()
        for (i in weights.indices) {
            if (weights[i] != 0u.toUByte()) {
                validActions.add(i)
            }
        }
        return validActions[Random.nextInt(validActions.size)]
    }
}

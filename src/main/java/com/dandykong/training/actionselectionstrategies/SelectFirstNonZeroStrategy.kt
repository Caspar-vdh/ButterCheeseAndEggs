package com.dandykong.training.actionselectionstrategies

import com.dandykong.butter.exception.ButterException
import com.dandykong.logger.ButterLogger

@Suppress("OPT_IN_USAGE", "OPT_IN_OVERRIDE")
class SelectFirstNonZeroStrategy(log: ButterLogger?): ActionSelectionStrategy(log) {
    override fun selectAction(weights: UByteArray): Int {
        log?.info("Using ${this.javaClass.simpleName}")
        for (i in weights.indices) {
            if (weights[i] != 0.toUByte()) {
                return i
            }
        }
        throw ButterException("No actions with non-zero weight")
    }
}


package com.dandykong.butter.game.action

import com.dandykong.butter.exception.ButterException

@Suppress("OPT_IN_USAGE", "OPT_IN_OVERRIDE")
class SelectFirstNonZeroStrategy: ActionSelectionStrategy {
    override fun selectAction(weights: UByteArray): Int {
        for (i in weights.indices) {
            if (weights[i] != 0.toUByte()) {
                return i
            }
        }
        throw ButterException("No actions with non-zero weight")
    }
}


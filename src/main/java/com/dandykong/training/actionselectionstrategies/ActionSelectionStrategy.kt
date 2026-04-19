package com.dandykong.training.actionselectionstrategies

import com.dandykong.logger.ButterLogger

abstract class ActionSelectionStrategy(var log: ButterLogger?) {

    @OptIn(ExperimentalUnsignedTypes::class)
    abstract fun selectAction(weights: UByteArray): Int
}

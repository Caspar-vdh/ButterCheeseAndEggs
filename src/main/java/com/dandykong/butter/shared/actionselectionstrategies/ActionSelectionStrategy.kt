package com.dandykong.butter.shared.actionselectionstrategies

import com.dandykong.logger.ButterLogger

abstract class ActionSelectionStrategy(var log: ButterLogger?) {

    @OptIn(ExperimentalUnsignedTypes::class)
    abstract fun selectAction(weights: UByteArray): Int
}

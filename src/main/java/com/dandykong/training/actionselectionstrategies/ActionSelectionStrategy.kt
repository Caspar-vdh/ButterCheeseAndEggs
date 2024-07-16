package com.dandykong.training.actionselectionstrategies

interface ActionSelectionStrategy {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun selectAction(weights: UByteArray): Int
}

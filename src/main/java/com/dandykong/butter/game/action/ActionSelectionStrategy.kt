package com.dandykong.butter.game.action

interface ActionSelectionStrategy {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun selectAction(weights: UByteArray): Int
}

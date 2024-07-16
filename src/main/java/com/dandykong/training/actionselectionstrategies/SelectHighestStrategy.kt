package com.dandykong.training.actionselectionstrategies

class SelectHighestStrategy: ActionSelectionStrategy {
    @ExperimentalUnsignedTypes
    override fun selectAction(weights: UByteArray): Int {
        var highest: UByte = 0u

        var action = 0
        for (index in weights.indices) {
            if (weights[index] > highest) {
                highest = weights[index]
                action = index
            }
        }
        return action
    }
}

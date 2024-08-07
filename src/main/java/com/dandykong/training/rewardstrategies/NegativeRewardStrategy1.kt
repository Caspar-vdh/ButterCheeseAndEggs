package com.dandykong.training.rewardstrategies

import com.dandykong.training.basics.MIN_WEIGHT
import com.dandykong.training.basics.State
import kotlin.math.max

class NegativeRewardStrategy1<S: State>: RewardStrategy<S> {

    /**
     * Final state gets decreased with 5 (or set to the min value, 0)
     * Second last state (given there are two or more entries) with 4
     * etc... up to fifth last state (given there are 5 states) with 1
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun updateWeights(selectedActions: Map<S, Int>) {
        var negativeRewardValue = 5

        for (entry in selectedActions.entries.reversed().iterator()) {
            val state = entry.key
            val action = entry.value

            val weight = state.weights[action].toInt()
            // TODO: error handling in case there are no weights for state?
            val newWeight = max(weight - negativeRewardValue, MIN_WEIGHT.toInt())
            state.weights[action] = newWeight.toUByte()
            negativeRewardValue--
        }
    }
}

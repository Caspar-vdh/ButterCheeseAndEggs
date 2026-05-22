package com.dandykong.butter.shared.rewardstrategies

import com.dandykong.butter.shared.state.MAX_WEIGHT
import com.dandykong.butter.shared.state.State
import kotlin.collections.iterator
import kotlin.math.min

class PositiveRewardStrategy1<IdType, S : State<IdType>>: RewardStrategy<IdType, S> {
    /**
     * Final state gets increased with 5 (or set to the max value, 255)
     * Second last state (given there are two or more entries) with 4
     * etc... up to fifth last state (given there are 5 states) with 1
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun updateWeights(selectedActions: Map<S, Int>) {
        var rewardValue = 5

        for (entry in selectedActions.entries.reversed().iterator()) {
            val state = entry.key
            val action = entry.value

            val weight = state.weights[action].toInt()
            // TODO: error handling in case there are no weights for state?
            val newWeight = min(weight + rewardValue, MAX_WEIGHT.toInt())
            state.weights[action] = newWeight.toUByte()
            rewardValue--
        }
    }
}

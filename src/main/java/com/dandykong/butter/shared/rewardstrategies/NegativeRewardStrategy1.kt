package com.dandykong.butter.shared.rewardstrategies

import com.dandykong.butter.shared.state.MIN_WEIGHT
import com.dandykong.butter.shared.state.State
import kotlin.math.max

class NegativeRewardStrategy1<IdType, S: State<IdType>>(
    private val nrRewardedActions: Int = DEFAULT_NR_REWARDED_ACTIONS
): RewardStrategy<IdType, S> {

    init {
        require(nrRewardedActions > 0) { "nrRewardedActions must be positive: $nrRewardedActions" }
    }

    /**
     * Final state gets decreased with nrRewardedActions (or set to the min value, 1)
     * Second last state (given there are two or more entries) with nrRewardedActions - 1
     * etc... down to 1. Earlier states are left unchanged.
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun updateWeights(selectedActions: Map<S, Int>) {
        var negativeRewardValue = nrRewardedActions

        for ((state, action) in selectedActions.entries.reversed().take(nrRewardedActions)) {
            val weight = state.weights[action].toInt()
            // TODO: error handling in case there are no weights for state?
            val newWeight = max(weight - negativeRewardValue, MIN_WEIGHT.toInt())
            state.weights[action] = newWeight.toUByte()
            negativeRewardValue--
        }
    }
}

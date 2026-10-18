package com.dandykong.butter.shared.rewardstrategies

import com.dandykong.butter.shared.state.MAX_WEIGHT
import com.dandykong.butter.shared.state.State
import kotlin.math.min

class PositiveRewardStrategy1<IdType, S : State<IdType>>(
    private val nrRewardedActions: Int = DEFAULT_NR_REWARDED_ACTIONS
): RewardStrategy<IdType, S> {

    init {
        require(nrRewardedActions > 0) { "nrRewardedActions must be positive: $nrRewardedActions" }
    }

    /**
     * Final state gets increased with nrRewardedActions (or set to the max value, 255)
     * Second last state (given there are two or more entries) with nrRewardedActions - 1
     * etc... down to 1. Earlier states are left unchanged.
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    override fun updateWeights(selectedActions: Map<S, Int>) {
        var rewardValue = nrRewardedActions

        for ((state, action) in selectedActions.entries.reversed().take(nrRewardedActions)) {
            val weight = state.weights[action].toInt()
            // TODO: error handling in case there are no weights for state?
            val newWeight = min(weight + rewardValue, MAX_WEIGHT.toInt())
            state.weights[action] = newWeight.toUByte()
            rewardValue--
        }
    }
}

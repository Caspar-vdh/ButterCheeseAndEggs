package com.dandykong.training.rewardstrategies

import com.dandykong.training.basics.RewardStrategy
import com.dandykong.training.basics.State

class PositiveRewardStrategy1: RewardStrategy<State> {

    /**
     * Final state gets increased with 5 (or set to the max value, 255)
     * Second last state (given there are two or more entries) with 4
     * etc... up to fifth last state (given there are 5 states) with 1
     */
    override fun updateWeights(weights: Map<State, UByteArray>) {
        TODO("Not yet implemented")
    }
}

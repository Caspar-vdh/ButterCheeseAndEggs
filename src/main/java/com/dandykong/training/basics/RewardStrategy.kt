package com.dandykong.training.basics

interface RewardStrategy<StateType> {

    /**
     * Updates the weights for all states in a game.
     * @param weightsForStates entries should be in order of insertion, i.e. the final entry should be for the last
     * state in the training run
     *
     */
    fun updateWeights(weightsForStates: Map<StateType, UByteArray>)
}

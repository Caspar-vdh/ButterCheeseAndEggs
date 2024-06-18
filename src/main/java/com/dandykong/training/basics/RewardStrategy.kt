package com.dandykong.training.basics

interface RewardStrategy<State> {

    /**
     * Updates the weights for all states in a game.
     *
     * @param selectedActions map, in which the keys are the states that have been encountered during a training
     * session, the values the actions that were selected for these states. Entries should be in order of insertion,
     * i.e. the final entry should be for the last state, encountered in the training session
     * @param weightsMap
     *
     */
    fun updateWeights(selectedActions: Map<State, Int>)
}

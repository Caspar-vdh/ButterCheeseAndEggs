package com.dandykong.butter.game

import com.dandykong.butter.game.action.ActionSelectionStrategy
import com.dandykong.training.basics.State

class Player<S : State>(val id: Int, private val strategy: ActionSelectionStrategy) {
    val selectedActions: MutableMap<S, Int> = hashMapOf()

    fun resetForNewGame() {
        selectedActions.clear()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun nextAction(state: S): Int {
        val action = strategy.selectAction(state.weights)
        selectedActions[state] = action
        return action
    }
}

package com.dandykong.training.player

import com.dandykong.training.actionselectionstrategies.ActionSelectionStrategy
import com.dandykong.training.basics.State

class CPUPlayer<S : State>(override val id: Int, private val strategy: ActionSelectionStrategy): Player<S> {
    val selectedActions: MutableMap<S, Int> = hashMapOf()
    override val type: Player.Type = Player.Type.CPU_PLAYER

    override fun resetForNewGame() {
        selectedActions.clear()
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun nextAction(state: S): Int {
        val action = strategy.selectAction(state.weights)
        selectedActions[state] = action
        return action
    }
}

package com.dandykong.butter.shared.player

import com.dandykong.butter.shared.actionselectionstrategies.ActionSelectionStrategy
import com.dandykong.butter.shared.state.State

class CPUPlayer<IdType, S : State<IdType>>(override val id: Int, private val strategy: ActionSelectionStrategy): Player<IdType, S> {
    // Insertion-ordered: reward strategies rely on the last entry being the last move
    val selectedActions: MutableMap<S, Int> = linkedMapOf()
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

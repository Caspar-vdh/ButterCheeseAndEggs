package com.dandykong.butter.game

import com.dandykong.butter.game.action.ActionSelectionStrategy

class Player(val id: Int, private val strategy: ActionSelectionStrategy) {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun nextAction(state: GridState): Int {
        return strategy.selectAction(state.weights)
    }
}

package com.dandykong.training.player

import com.dandykong.training.basics.State

class HumanPlayer<S : State>(override val id: Int) : Player<S> {
    override fun resetForNewGame() {
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun nextAction(state: S): Int {
        val validActions = mutableListOf<Int>()
        for (i in state.weights.indices) {
            if (state.weights[i] != 0u.toUByte()) {
                validActions.add(i)
            }
        }

        while (true) {
            println("Select an action from ${validActions.joinToString()}")
            val actionChar = System.`in`.read().toChar()
            if (actionChar.isDigit()) {
                val tempAction = actionChar.digitToInt()
                if (validActions.contains(tempAction)) {
                    return tempAction
                }
            }
        }
    }

}

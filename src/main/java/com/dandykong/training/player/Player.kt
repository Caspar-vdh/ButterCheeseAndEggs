package com.dandykong.training.player

import com.dandykong.training.basics.State

interface Player<S: State> {
    val id: Int
    fun resetForNewGame()
    fun nextAction(state: S): Int

    companion object {
        const val PLAYER_1 = 1
        const val PLAYER_2 = 2
        const val CPU_PLAYER = 3
        const val HUMAN_PLAYER = 4
    }
}
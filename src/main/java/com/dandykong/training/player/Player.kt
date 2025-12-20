package com.dandykong.training.player

import com.dandykong.training.basics.State

interface Player<S: State> {
    val id: Int
    val type: Type
    fun resetForNewGame()
    fun nextAction(state: S): Int

    enum class Type {
        CPU_PLAYER, HUMAN_PLAYER
    }

    companion object {
        const val PLAYER_1 = 1
        const val PLAYER_2 = 2
    }
}
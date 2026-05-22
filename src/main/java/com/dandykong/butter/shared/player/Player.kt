package com.dandykong.butter.shared.player

import com.dandykong.butter.shared.state.State

interface Player<IdType, S: State<IdType>> {
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
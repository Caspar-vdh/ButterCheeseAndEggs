package com.dandykong.butter.shared.game

import com.dandykong.butter.shared.game.grid.Grid
import com.dandykong.butter.shared.player.Player

fun interface GameStateListener {
    fun onStateChanged(state: GameState)
}

interface GameGridListener<IdType> {
    fun onGridUpdated(grid: Grid<IdType>, row: Int, column: Int)
}

interface GameEventListener {
    fun onGameStarted(firstPlayerType: Player.Type)
    fun onGameTerminated(winningPlayerType: Player.Type?)
}
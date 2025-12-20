package com.dandykong.butter.game

import com.dandykong.butter.game.grid.Grid
import com.dandykong.training.player.Player

fun interface GameStateListener {
    fun onStateChanged(state: GameState)
}

interface GameGridListener {
    fun onGridUpdated(grid: Grid, row: Int, column: Int)
}

interface GameEventListener {
    fun onGameStarted(firstPlayerType: Player.Type)
    fun onGameTerminated(winningPlayerType: Player.Type?)
}
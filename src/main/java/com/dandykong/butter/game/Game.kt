package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.ui.ConsoleDrawer
import com.dandykong.butter.ui.GridDrawer
import com.dandykong.training.player.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Game {

    fun play2() {
        val gameFacade = GameFacade()
        val drawer: GridDrawer = ConsoleDrawer()
        val gameFinished = AtomicBoolean(false)

        gameFacade.gameStateListener = GameStateListener { state ->
            when(state) {
                GameState.WAITING_FOR_PLAYER -> {
                    var done = false
                    while (! done) {
                        var row = -1
                        var column = -1
                        try {
                            row = getValue("row")
                            column = getValue("column")
                            gameFacade.processMove(row, column)
                            done = true
                        } catch (ex: ButterException) {
                            println("Invalid cell: [row: $row, column: $column]")
                        }
                    }
                }

                else -> { /* Do nothing */ }
            }
        }

        gameFacade.gameGridListener = object : GameGridListener {
            override fun onGridUpdated(grid: Grid, row: Int, column: Int) {
                drawer.draw(grid)
            }
        }

        gameFacade.gameEventListener = object : GameEventListener {
            override fun onGameStarted(firstPlayerType: Player.Type) {
                val startedString = when(firstPlayerType) {
                    Player.Type.HUMAN_PLAYER -> "You go first."
                    Player.Type.CPU_PLAYER -> "The computer goes first."
                }
                println("The game has started. $startedString")
            }

            override fun onGameTerminated(winningPlayerType: Player.Type?) {
                val winnerString = when(winningPlayerType) {
                    null -> "Nobody"
                    Player.Type.HUMAN_PLAYER -> "You"
                    Player.Type.CPU_PLAYER -> "The computer"
                }
                println("The game has finished. $winnerString won!")
                gameFinished.set(true)
            }
        }
        gameFacade.startGame()
        while (!gameFinished.get()) {
            runBlocking { delay(100) }
        }
    }

    companion object {
        private fun getValue(valueType: String): Int {
            while (true) {
                println("Select $valueType [0..2]")

                val scanner = Scanner(System.`in`)
                val c = scanner.next().single()
                val tempVal = c.digitToInt()
                if (tempVal in 0..2) {
                    return tempVal
                }
            }
        }
    }
}

fun main() {
    Game().play2()
}
package com.dandykong

import ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME
import com.dandykong.butter.shared.exception.ButterException
import com.dandykong.butter.shared.game.GameEventListener
import com.dandykong.butter.tictactoe.game.GameFacade
import com.dandykong.butter.shared.game.GameGridListener
import com.dandykong.butter.shared.game.GameState
import com.dandykong.butter.shared.game.GameStateListener
import com.dandykong.butter.tictactoe.game.TicTacToeGridState
import com.dandykong.butter.tictactoe.game.Training
import com.dandykong.butter.shared.game.grid.Grid
import com.dandykong.butter.tictactoe.ui.ConsoleDrawer
import com.dandykong.butter.shared.ui.GridDrawer
import com.dandykong.logger.ButterLogger
import com.dandykong.training.actionselectionstrategies.MultipleSelectionStrategy
import com.dandykong.training.actionselectionstrategies.RandomSelectionStrategy
import com.dandykong.training.actionselectionstrategies.SelectChanceByWeightStrategy
import com.dandykong.training.actionselectionstrategies.SelectFirstNonZeroStrategy
import com.dandykong.training.player.CPUPlayer
import com.dandykong.training.player.Player
import com.dandykong.training.rewardstrategies.NegativeRewardStrategy1
import com.dandykong.training.rewardstrategies.PositiveRewardStrategy1
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration.Companion.milliseconds

class Game {

    private val log = object : ButterLogger {
        private val logger: Logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME)

        override fun info(var1: String) {
            logger.info(var1)
        }

        override fun info(var1: String, var2: Any) {
            logger.info(var1, var2)
        }

        override fun info(var1: String, var2: Any, var3: Any) {
            logger.info(var1, var2, var3)
        }

        override fun info(var1: String, vararg var2: Any) {
            logger.info(var1, var2)
        }
    }

    @Suppress("unused")
    fun train() {
//    val strategy = SelectChanceByWeightStrategy()
        val strategy = MultipleSelectionStrategy(
            Pair(SelectChanceByWeightStrategy(), 12),
            Pair(RandomSelectionStrategy(log), 2),
            Pair(SelectFirstNonZeroStrategy(log), 1)
        )

        val players: Array<CPUPlayer<TicTacToeGridState>> = arrayOf(
            CPUPlayer(Player.PLAYER_1, strategy),
            CPUPlayer(Player.PLAYER_2, strategy),
        )

        Training(players, PositiveRewardStrategy1(), NegativeRewardStrategy1(), log).play()
    }


    fun play() {
        val gameFacade = GameFacade(log)
        val drawer: GridDrawer<Int> = ConsoleDrawer()
        val gameFinished = AtomicBoolean(false)

        gameFacade.gameStateListener = GameStateListener { state ->
            when (state) {
                GameState.WAITING_FOR_PLAYER -> {
                    var done = false
                    while (!done) {
                        var row = -1
                        var column = -1
                        try {
                            row = getValue("row")
                            column = getValue("column")
                            gameFacade.processMove(row, column)
                            done = true
                        } catch (_: ButterException) {
                            println("Invalid cell: [row: $row, column: $column]")
                        }
                    }
                }

                else -> { /* Do nothing */
                }
            }
        }

        gameFacade.gameGridListener = object : GameGridListener<Int> {
            override fun onGridUpdated(grid: Grid<Int>, row: Int, column: Int) {
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
            runBlocking { delay(100.milliseconds) }
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
    Game().play()
}

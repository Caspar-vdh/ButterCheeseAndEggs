package com.dandykong.butter.tictactoe.game

import com.dandykong.butter.shared.exception.ButterException
import com.dandykong.butter.tictactoe.state.TicTacToeGridStateFactory
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_ROWS
import com.dandykong.butter.tictactoe.game.grid.TicTacToeGrid
import com.dandykong.butter.tictactoe.state.TicTacToeGridState
import com.dandykong.butter.tictactoe.state.actionIdToRowAndColumn
import com.dandykong.butter.tictactoe.ui.ConsoleDrawer
import com.dandykong.logger.ButterLogger
import com.dandykong.butter.shared.player.CPUPlayer
import com.dandykong.butter.shared.rewardstrategies.RewardStrategy
import com.dandykong.butter.tictactoe.state.TicTacToeStateStore
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream

const val NR_OF_GAMES = 100000
const val NR_OF_GAMES_FOR_STORE = 200

private const val FILE_PATH = "C:/Users/c_van/Projects/data/ButterCheeseAndEggs/training.dat"

class Training(
    private val players: Array<CPUPlayer<Int, TicTacToeGridState>>,
    private val positiveRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
    private val negativeRewardStrategy: RewardStrategy<Int, TicTacToeGridState>
) {

    constructor(
        players: Array<CPUPlayer<Int, TicTacToeGridState>>,
        positiveRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
        negativeRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
        log: ButterLogger
    ) : this(players, positiveRewardStrategy, negativeRewardStrategy) {
        this.log = log
    }

    var log: ButterLogger? = null

    fun play() {
        val stateStore = TicTacToeStateStore(
            DataInputStream(FileInputStream(FILE_PATH)),
            NR_GRID_ROWS * NR_GRID_COLUMNS,
            TicTacToeGridStateFactory()
        )

        val drawer: ConsoleDrawer? = null

        for (i in 1..NR_OF_GAMES) {
            playGame(i, stateStore, drawer)
            val stream = DataOutputStream(FileOutputStream(FILE_PATH))
            if (i.mod(NR_OF_GAMES_FOR_STORE) == 0) stateStore.persistStore(stream)
        }
    }

    private fun playGame(gameIndex: Int, stateStore: TicTacToeStateStore, drawer: ConsoleDrawer?) {
        for (player in players) player.resetForNewGame()
        val grid = TicTacToeGrid()
        var terminate = false

        var nrExistingStates = 0
        var nrNewStates = 0

        while (!terminate) {
            try {
                for (player in players) {
                    val id = grid.generateId(player.id)
                    val state =
                        if (stateStore.hasStateForId(id)) {
                            nrExistingStates++
                            stateStore.getStateForId(id)!!
                        } else {
                            val s = TicTacToeGridState.createNewFromGrid(grid, id)
                            nrNewStates++
                            stateStore.addState(s)
                            s
                        }
                    val nextAction = player.nextAction(state)
                    val (row, column) = actionIdToRowAndColumn(nextAction)
                    grid.setCell(row, column, player.id)
                    drawer?.draw(grid)
                    val winningPlayer = grid.winningPlayer
                    if (winningPlayer != null) {
                        log?.info("Game $gameIndex: $winningPlayer won, $nrExistingStates existing states, $nrNewStates new states")
                        for (p in players) {
                            if (p.id == winningPlayer) positiveRewardStrategy.updateWeights(p.selectedActions)
                            else negativeRewardStrategy.updateWeights(p.selectedActions)
                        }
                        terminate = true
                        break
                    }
                    if (grid.isFull()) {
                        log?.info("Game $gameIndex: no winner, $nrExistingStates existing states, $nrNewStates new states")
                        terminate = true
                        break
                    }
                    drawer?.waitForUser()
                }
            } catch (_: ButterException) {
                terminate = true
            }
        }
    }
}
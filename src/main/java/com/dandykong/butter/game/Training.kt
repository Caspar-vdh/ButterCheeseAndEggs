package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.game.grid.GridStateFactory
import com.dandykong.butter.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.game.grid.NR_GRID_ROWS
import com.dandykong.training.player.CPUPlayer
import com.dandykong.butter.ui.GridDrawer
import com.dandykong.logger.ButterLogger
import com.dandykong.training.rewardstrategies.RewardStrategy
import com.dandykong.training.basics.StateStore
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream

const val NR_OF_GAMES = 100000
const val NR_OF_GAMES_FOR_STORE = 200

private const val FILE_PATH = "C:/Users/c_van/Projects/data/ButterCheeseAndEggs/training.dat"

class Training(
    private val players: Array<CPUPlayer<GridState>>,
    private val positiveRewardStrategy: RewardStrategy<GridState>,
    private val negativeRewardStrategy: RewardStrategy<GridState>
) {

    constructor(
        players: Array<CPUPlayer<GridState>>,
        positiveRewardStrategy: RewardStrategy<GridState>,
        negativeRewardStrategy: RewardStrategy<GridState>,
        log: ButterLogger) : this(players, positiveRewardStrategy, negativeRewardStrategy) {
        this.log = log
    }

    var log: ButterLogger? = null

    fun play() {
        val stateStore = StateStore(
            DataInputStream(FileInputStream(FILE_PATH)),
            NR_GRID_ROWS * NR_GRID_COLUMNS,
            GridStateFactory()
        )

        val drawer: GridDrawer? = null

        for (i in 1..NR_OF_GAMES) {
            playGame(i, stateStore, drawer)
            val stream = DataOutputStream(FileOutputStream(FILE_PATH))
            if (i.mod(NR_OF_GAMES_FOR_STORE) == 0) stateStore.persistStore(stream)
        }
    }

    private fun playGame(gameIndex: Int, stateStore: StateStore<GridState>, drawer: GridDrawer?) {
        for (player in players) player.resetForNewGame()
        val grid = Grid.createInitial()
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
                            val s = GridState.createNewFromGrid(grid, id)
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
            } catch (ex: ButterException) {
                terminate = true
            }
        }
    }
}

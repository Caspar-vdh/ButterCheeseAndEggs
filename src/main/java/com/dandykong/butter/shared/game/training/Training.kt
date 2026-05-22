package com.dandykong.butter.shared.game.training

import com.dandykong.butter.shared.player.CPUPlayer
import com.dandykong.butter.shared.rewardstrategies.RewardStrategy
import com.dandykong.butter.shared.state.State
import com.dandykong.butter.shared.state.StateStore
import com.dandykong.butter.shared.ui.GridDrawer
import com.dandykong.logger.ButterLogger
import java.io.DataOutputStream
import java.io.FileOutputStream

@Suppress("unused")
class Training<IdType, S: State<IdType>>(
    private val configuration: TrainingConfiguration,
    private val players: Array<CPUPlayer<IdType, S>>,
    private val positiveRewardStrategy: RewardStrategy<IdType, S>,
    private val negativeRewardStrategy: RewardStrategy<IdType, S>,
    private val stateStore: StateStore<S, IdType>,
    private val drawer: GridDrawer<IdType>?
) {

    constructor(
        configuration: TrainingConfiguration,
        players: Array<CPUPlayer<IdType, S>>,
        positiveRewardStrategy: RewardStrategy<IdType, S>,
        negativeRewardStrategy: RewardStrategy<IdType, S>,
        stateStore: StateStore<S, IdType>,
        drawer: GridDrawer<IdType>?,
        log: ButterLogger
    ) : this(configuration, players, positiveRewardStrategy, negativeRewardStrategy, stateStore, drawer) {
        this.log = log
    }

    var log: ButterLogger? = null

    fun play() {
        val stream = DataOutputStream(FileOutputStream(configuration.filePath))
        for (i in 1..configuration.nrOfGames) {
            playGame(i)
            if (i.mod(configuration.nrOfGamesForStore) == 0) stateStore.persistStore(stream)
        }
    }

    private fun playGame(gameIndex: Int) {
//        for (player in players) player.resetForNewGame()
//        val grid = TicTacToeGrid()
//        var terminate = false
//
//        var nrExistingStates = 0
//        var nrNewStates = 0
//
//        while (!terminate) {
//            try {
//                for (player in players) {
//                    val id = grid.generateId(player.id)
//                    val state =
//                        if (stateStore.hasStateForId(id)) {
//                            nrExistingStates++
//                            stateStore.getStateForId(id)!!
//                        } else {
//                            val s = TicTacToeGridState.createNewFromGrid(grid, id)
//                            nrNewStates++
//                            stateStore.addState(s)
//                            s
//                        }
//                    val nextAction = player.nextAction(state)
//                    val (row, column) = actionIdToRowAndColumn(nextAction)
//                    grid.setCell(row, column, player.id)
//                    drawer?.draw(grid)
//                    val winningPlayer = grid.winningPlayer
//                    if (winningPlayer != null) {
//                        log?.info("Game $gameIndex: $winningPlayer won, $nrExistingStates existing states, $nrNewStates new states")
//                        for (p in players) {
//                            if (p.id == winningPlayer) positiveRewardStrategy.updateWeights(p.selectedActions)
//                            else negativeRewardStrategy.updateWeights(p.selectedActions)
//                        }
//                        terminate = true
//                        break
//                    }
//                    if (grid.isFull()) {
//                        log?.info("Game $gameIndex: no winner, $nrExistingStates existing states, $nrNewStates new states")
//                        terminate = true
//                        break
//                    }
//                    drawer?.waitForUser()
//                }
//            } catch (_: ButterException) {
//                terminate = true
//            }
//        }
    }
}
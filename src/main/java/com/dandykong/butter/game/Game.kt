package com.dandykong.butter.game

import ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME
import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.action.SelectChanceByWeightStrategy
import com.dandykong.butter.game.grid.GridStateFactory
import com.dandykong.butter.ui.ConsoleDrawer
import com.dandykong.butter.ui.GridDrawer
import com.dandykong.logger.LOG
import com.dandykong.training.basics.RewardStrategy
import com.dandykong.training.basics.StateStore
import com.dandykong.training.rewardstrategies.NegativeRewardStrategy1
import com.dandykong.training.rewardstrategies.PositiveRewardStrategy1
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val NR_OF_GAMES = 100000
const val NR_OF_GAMES_FOR_STORE = 200

class Game(
    private val players: Array<Player<GridState>>,
    private val positiveRewardStrategy: RewardStrategy<GridState>,
    private val negativeRewardStrategy: RewardStrategy<GridState>
) {
    fun play() {
        val stateStore = StateStore(
            "C:/Users/c_van/Projects/data/ButterCheeseAndEggs/training.dat",
            NR_GRID_ROWS * NR_GRID_COLUMNS,
            GridStateFactory()
        )

        val drawer: GridDrawer? = null
//        val drawer: GridDrawer? = ConsoleDrawer()

        for (i in 1..NR_OF_GAMES) {
            playGame(i, stateStore, drawer)
            if (i.mod(NR_OF_GAMES_FOR_STORE) == 0) stateStore.persistStore()
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
                        LOG.info("Game $gameIndex: $winningPlayer won, $nrExistingStates existing states, $nrNewStates new states")
                        for (p in players) {
                            if (p.id == winningPlayer) positiveRewardStrategy.updateWeights(p.selectedActions)
                            else negativeRewardStrategy.updateWeights(p.selectedActions)
                        }
                        terminate = true
                        break
                    }
                    if (grid.isFull()) {
                        LOG.info("Game $gameIndex: no winner, $nrExistingStates existing states, $nrNewStates new states")
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

fun main() {
    val players: Array<Player<GridState>> = arrayOf(
        Player(Grid.PLAYER_1, SelectChanceByWeightStrategy()),
        Player(Grid.PLAYER_2, SelectChanceByWeightStrategy()),
    )

    Game(players, PositiveRewardStrategy1(), NegativeRewardStrategy1()).play()
}

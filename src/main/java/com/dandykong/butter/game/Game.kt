package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.action.SelectChanceByWeightStrategy
import com.dandykong.butter.game.grid.GridStateFactory
import com.dandykong.butter.ui.ConsoleDrawer
import com.dandykong.training.basics.RewardStrategy
import com.dandykong.training.basics.StateStore
import com.dandykong.training.rewardstrategies.NegativeRewardStrategy1
import com.dandykong.training.rewardstrategies.PositiveRewardStrategy1

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

        for (player in players) player.resetForNewGame()
        val grid = Grid.createInitial()
        val drawer = ConsoleDrawer(grid)
        var terminate = false
        while (!terminate) {
            try {
                for (player in players) {
                    val id = grid.generateId(player.id)
                    val state =
                        if (stateStore.hasStateForId(id)) {
                            stateStore.getStateForId(id)!!
                        } else {
                            val s = GridState.createNewFromGrid(grid, id)
                            stateStore.addState(s)
                            s
                        }
                    val nextAction = player.nextAction(state)
                    val (row, column) = actionIdToRowAndColumn(nextAction)
                    grid.setCell(row, column, player.id)
                    drawer.draw()
                    val winningPlayer = grid.winningPlayer
                    if (winningPlayer != null) {
                        println("Game finished, $winningPlayer won!")
                        for (player in players) {
                            if (player.id == winningPlayer) positiveRewardStrategy.updateWeights(player.selectedActions)
                            else negativeRewardStrategy.updateWeights(player.selectedActions)
                        }
                        terminate = true
                        break
                    }
                    if (grid.isFull()) {
                        println("Game over, nobody won")
                        terminate = true
                        break
                    }
                    readln()
                }
            } catch (ex: ButterException) {
                terminate = true
            }
        }
        stateStore.persistStore()
    }
}

fun main() {
    val players: Array<Player<GridState>> = arrayOf(
        Player(Grid.PLAYER_1, SelectChanceByWeightStrategy()),
        Player(Grid.PLAYER_2, SelectChanceByWeightStrategy()),
    )

    Game(players, PositiveRewardStrategy1(), NegativeRewardStrategy1()).play()
}

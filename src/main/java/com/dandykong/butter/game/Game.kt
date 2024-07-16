package com.dandykong.butter.game

import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.game.grid.GridStateFactory
import com.dandykong.butter.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.game.grid.NR_GRID_ROWS
import com.dandykong.butter.ui.ConsoleDrawer
import com.dandykong.butter.ui.GridDrawer
import com.dandykong.logger.LOG
import com.dandykong.training.actionselectionstrategies.SelectHighestStrategy
import com.dandykong.training.basics.StateStore
import com.dandykong.training.player.CPUPlayer
import com.dandykong.training.player.HumanPlayer
import com.dandykong.training.player.Player
import kotlin.random.Random

class Game(private val players: Array<Player<GridState>>) {
    fun play() {
        val stateStore = StateStore(
            "C:/Users/c_van/Projects/data/ButterCheeseAndEggs/training.dat",
            NR_GRID_ROWS * NR_GRID_COLUMNS,
            GridStateFactory()
        )
        var terminate = false
        val drawer: GridDrawer = ConsoleDrawer()
        val grid = Grid.createInitial()
        while (!terminate) {
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
                drawer.draw(grid)
                val winningPlayer = grid.winningPlayer
                if (winningPlayer != null) {
                    println("Player $winningPlayer won")
                    terminate = true
                    break
                }
                if (grid.isFull()) {
                    LOG.info("No winner")
                    terminate = true
                    break
                }
            }
        }
    }
}

fun main() {
    val players =
        arrayOf(HumanPlayer<GridState>(Player.PLAYER_2), CPUPlayer<GridState>(Player.PLAYER_1, SelectHighestStrategy()))
    val random = Random(System.currentTimeMillis()).nextInt()
    if (random.mod(2) == 0) {
        players.reverse()
    }
    Game(players).play()
}
package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.action.SelectChanceByWeightStrategy
import com.dandykong.butter.game.grid.GridStateFactory
import com.dandykong.butter.ui.ConsoleDrawer
import com.dandykong.training.basics.StateStore

class Game(private val players: Array<Player>) {
    fun play() {

        val stateStore = StateStore<GridState>(
            "C:/Users/c_van/Projects/data/ButterCheeseAndEggs/training.dat",
            NR_GRID_ROWS * NR_GRID_COLUMNS,
            GridStateFactory()
        )

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
                    if (grid.winningPlayer != null) {
                        println("Game finished, ${grid.winningPlayer} won!")
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
    val players = arrayOf(
        Player(Grid.PLAYER_1, SelectChanceByWeightStrategy()),
        Player(Grid.PLAYER_2, SelectChanceByWeightStrategy()),
    )

    Game(players).play()
}

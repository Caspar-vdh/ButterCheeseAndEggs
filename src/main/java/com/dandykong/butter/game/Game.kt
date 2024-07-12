package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.action.SelectChanceByWeightStrategy
import com.dandykong.butter.ui.ConsoleDrawer

class Game(private val players: Array<Player>) {
    fun play() {
        val grid = Grid.createInitial()
        val drawer = ConsoleDrawer(grid)
        var terminate = false
        while (!terminate) {
            try {
                for (player in players) {
                    val id = grid.generateId(player.id)
                    val state = GridState.createNewFromGrid(grid, id)
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
    }
}

fun main() {
    val players = arrayOf(
        Player(Grid.PLAYER_1, SelectChanceByWeightStrategy()),
        Player(Grid.PLAYER_2, SelectChanceByWeightStrategy()),
    )

    Game(players).play()
}

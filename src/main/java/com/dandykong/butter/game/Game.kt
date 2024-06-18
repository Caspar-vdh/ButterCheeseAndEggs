package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.action.SelectChanceByWeightStrategy
import com.dandykong.butter.ui.ConsoleDrawer

class Game(private val players: Array<Player>, private val grid: Grid) {
    fun play() {
        grid.startGame(players[0].name, players[1].name)
        val drawer = ConsoleDrawer(grid)
        var terminate = false
        while (!terminate) {
            try {
                for (player in players) {
                    val id = GridState.generateId(grid, player.name)
                    val nextAction = player.nextAction(GridState.createNewFromGrid(grid, id))
                    val (row, column) = actionIdToRowAndColumn(nextAction)
                    grid.setCell(row, column, player.name)
                    drawer.draw()
                    if (grid.winningPlayer != null) {
                        println("Game finished, ${grid.winningPlayer} won!")
                        terminate = true
                        break
                    }
                    if (grid.filledCells == NR_GRID_ROWS * NR_GRID_COLUMNS) {
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
        Player("PLAYER 1", SelectChanceByWeightStrategy()),
        Player("PLAYER 2", SelectChanceByWeightStrategy()),
    )

    val grid = Grid()

    Game(players, grid).play()
}

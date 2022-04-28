package com.dandykong.butter.game

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.action.SelectFirstNonZeroStrategy
import com.dandykong.butter.ui.ConsoleDrawer

class Game(private val players: Array<Player>, private val grid: Grid) {
    fun play() {
        grid.startGame(players[0].name, players[1].name)
        val drawer = ConsoleDrawer(grid)
        var terminate = false
        @Suppress("KotlinConstantConditions")
        while (!terminate) {
            try {
                for (player in players) {
                    val nextAction = player.nextAction(GridState.fromGrid(grid, player.name))
                    val (row, column) = actionIdToRowAndColumn(nextAction)
                    grid.setCell(row, column, player.name)
                    drawer.draw()
                }
            } catch (ex: ButterException) {
                terminate = true
            }
        }
    }
}

fun main() {
    val players = arrayOf(
        Player("PLAYER 1", SelectFirstNonZeroStrategy()),
        Player("PLAYER 2", SelectFirstNonZeroStrategy()),
    )

    val grid = Grid()

    Game(players, grid).play()
}

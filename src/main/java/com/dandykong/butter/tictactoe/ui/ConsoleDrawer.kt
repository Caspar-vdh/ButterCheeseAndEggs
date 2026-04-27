package com.dandykong.butter.tictactoe.ui

import com.dandykong.butter.shared.exception.ButterException
import com.dandykong.butter.shared.game.grid.Grid
import com.dandykong.butter.shared.ui.GridDrawer
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_ROWS
import com.dandykong.training.player.Player

class ConsoleDrawer() : GridDrawer<Int>() {
    private val playerIcons = CharArray(2) { index -> when (index) {
        0 -> 'o'
        1 -> 'x'
        else -> throw ButterException("Unexpected index in playerIcons array: $index")
    } }

    override fun draw(grid: Grid<Int>) {
        println()
        for (row in 0 until NR_GRID_ROWS) {
            if (row > 0) {
                println("---+---+---")
            }
            println(lineForRow(row, grid))
        }
        println()
    }

    override fun waitForUser() {
        readln()
    }

    private fun lineForRow(row: Int, grid: Grid<Int>): String {
        var line = ""
        for (i in 0 until NR_GRID_COLUMNS) {
            if (i > 0) {
                line += " |"
            }
            line += when (val cell = grid.getCell(row, i)) {
                0 -> "  "
                Player.PLAYER_1 -> " ${playerIcons[0]}"
                Player.PLAYER_2 -> " ${playerIcons[1]}"
                else -> {
                    throw ButterException("Unexpected value for cell [$row, $i]: $cell")
                }
            }
        }
        return line
    }
}
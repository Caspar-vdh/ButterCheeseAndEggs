package com.dandykong.butter.ui

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.game.grid.NR_GRID_ROWS
import com.dandykong.training.player.Player

class ConsoleDrawer() : GridDrawer() {
    private val playerIcons = CharArray(2) { index -> when (index) {
        0 -> 'o'
        1 -> 'x'
        else -> throw ButterException("Unexpected index in playerIcons array: $index")
    } }

    override fun draw(grid: Grid) {
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

    private fun lineForRow(row: Int, grid: Grid): String {
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

package com.dandykong.butter.ui

import com.dandykong.butter.exception.ButterException
import com.dandykong.butter.game.Grid
import com.dandykong.butter.game.NR_GRID_COLUMNS
import com.dandykong.butter.game.NR_GRID_ROWS

class ConsoleDrawer(grid: Grid) : GridDrawer(grid) {
    private val playerIcons = CharArray(2) { index -> when (index) {
        0 -> 'o'
        1 -> 'x'
        else -> throw ButterException("Unexpected index in playerIcons array: $index")
    } }

    override fun draw() {
        println()
        for (row in 0 until NR_GRID_ROWS) {
            if (row > 0) {
                println("---+---+---")
            }
            println(lineForRow(row))
        }
        println()
    }

    private fun lineForRow(row: Int): String {
        var line = ""
        for (i in 0 until NR_GRID_COLUMNS) {
            if (i > 0) {
                line += " |"
            }
            line += when (val cell = grid.getCell(row, i)) {
                0 -> "  "
                Grid.PLAYER_1 -> " ${playerIcons[0]}"
                Grid.PLAYER_2 -> " ${playerIcons[1]}"
                else -> {
                    throw ButterException("Unexpected value for cell [$row, $i]: $cell")
                }
            }
        }
        return line
    }
}

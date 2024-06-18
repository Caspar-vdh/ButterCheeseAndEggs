package com.dandykong.butter.game

import com.dandykong.training.basics.INITIAL_WEIGHT
import com.dandykong.training.basics.State

const val MAX_NR_ACTIONS = NR_GRID_ROWS * NR_GRID_COLUMNS

@OptIn(ExperimentalUnsignedTypes::class)
class GridState(id: Int, weights: UByteArray): State(id, weights) {
    companion object {

        fun createNewFromGrid(grid: Grid, id: Int): GridState {
            val weights = UByteArray(MAX_NR_ACTIONS) { 0u }
            for (row in 0 until NR_GRID_ROWS) {
                for (column in 0 until NR_GRID_COLUMNS) {
                    val index = rowAndColumToActionId(row, column)
                    if (grid.isCellEmpty(row, column)) {
                        weights[index] = INITIAL_WEIGHT
                    }
                }
            }
            return GridState(id, weights)
        }

        fun generateId(grid: Grid, playerId: String): Int {
            var id = 0
            for (row in 0 until NR_GRID_ROWS) {
                for (column in 0 until NR_GRID_COLUMNS) {
                    val index = rowAndColumToActionId(row, column)
                    if (!grid.isCellEmpty(row, column)) {
                        val cellState =
                            if (grid.getCell(row, column).equals(playerId)) CellState.MINE
                            else CellState.THEIRS
                        id = id or (cellState.state shl (index * 2))
                    }
                }
            }
            return id
        }
    }
}

fun rowAndColumToActionId(row: Int, column: Int): Int {
    return row * NR_GRID_COLUMNS + column
}

fun actionIdToRowAndColumn(actionId: Int): Pair<Int, Int> {
    return Pair(actionId / NR_GRID_COLUMNS, actionId.mod(NR_GRID_COLUMNS))
}

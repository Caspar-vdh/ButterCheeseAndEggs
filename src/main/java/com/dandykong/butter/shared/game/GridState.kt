package com.dandykong.butter.shared.game

import com.dandykong.butter.shared.game.grid.Grid
import com.dandykong.butter.tictactoe.game.grid.NR_GRID_COLUMNS

@OptIn(ExperimentalUnsignedTypes::class)
fun <IdType> createWeights(grid: Grid<IdType>, nrRows: Int, nrCols: Int, initialWeight: UByte): UByteArray {
    val maxNrActions = nrRows * nrCols
    val weights = UByteArray(maxNrActions)
    for (row in 0 until nrRows) {
        for (column in 0 until nrCols) {
            val index = rowAndColumToActionId(row, column, NR_GRID_COLUMNS)
            if (grid.isCellEmpty(row, column)) {
                weights[index] = initialWeight
            }
        }
    }
    return weights
}

fun rowAndColumToActionId(row: Int, column: Int, nrCols: Int): Int {
    return row * nrCols + column
}

fun actionIdToRowAndColumn(actionId: Int, nrCols: Int): Pair<Int, Int> {
    return Pair(actionId / nrCols, actionId.mod(nrCols))
}
package com.dandykong.butter.game

const val INITIAL_WEIGHT: UByte = 127u

class GridState {
    private val cells: ByteArray;
    val weights: UByteArray = UByteArray(9) { 0u };

    private constructor(cells: ByteArray) {
        this.cells = cells
        for (i in 0 until 9) {
            if (cells[i] == CellState.EMPTY.state) {
                weights[i] = INITIAL_WEIGHT
            }
        }
    }

    companion object {
        fun fromGrid(grid: Grid, playerId: String): GridState {
            val cells = ByteArray(9) { CellState.EMPTY.state }
            for (row in 0 until NR_GRID_ROWS) {
                for (column in 0 until NR_GRID_COLUMNS) {
                    if (!grid.isCellEmpty(row, column)) {
                        if (grid.getCell(row, column).equals(playerId)) {
                            cells[rowAndColumToActionId(row, column)] = CellState.MINE.state
                        } else {
                            cells[rowAndColumToActionId(row, column)] = CellState.THEIRS.state
                        }
                    }
                }
            }
            return GridState(cells)
        }

    }
}

fun rowAndColumToActionId(row: Int, column: Int): Int {
    return row * NR_GRID_COLUMNS + column
}

fun actionIdToRowAndColumn(actionId: Int): Pair<Int, Int> {
    return Pair(actionId / NR_GRID_COLUMNS, actionId.mod(NR_GRID_COLUMNS))
}

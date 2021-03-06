package com.dandykong.butter.game

const val INITIAL_WEIGHT: UByte = 127u
const val MAX_NR_ACTIONS = NR_GRID_ROWS * NR_GRID_COLUMNS

@OptIn(ExperimentalUnsignedTypes::class)
class GridState private constructor(val cells: Array<Array<CellState>>) {

    val weights: UByteArray = UByteArray(MAX_NR_ACTIONS) { 0u }

    init {
        for (row in 0 until NR_GRID_ROWS) {
            for (column in 0 until NR_GRID_COLUMNS) {
                if (cells[row][column] == CellState.EMPTY) {
                    weights[rowAndColumToActionId(row, column)] = INITIAL_WEIGHT
                }
            }
        }
    }

    fun generateId(): Int {
        var id = 0
        for(i in 0 until MAX_NR_ACTIONS) {
            val (row, column) = actionIdToRowAndColumn(i)
            val cellState = cells[row][column].state
            id = id or (cellState shl (i * 2))
        }
        return id
    }

    companion object {
        fun fromGrid(grid: Grid, playerId: String): GridState {
            val cells:Array<Array<CellState>> = Array(NR_GRID_ROWS) { Array(NR_GRID_COLUMNS) { CellState.EMPTY } } // = ByteArray(9) { CellState.EMPTY.state }
            for (row in 0 until NR_GRID_ROWS) {
                for (column in 0 until NR_GRID_COLUMNS) {
                    if (!grid.isCellEmpty(row, column)) {
                        if (grid.getCell(row, column).equals(playerId)) {
                            cells[row][column] = CellState.MINE
                        } else {
                            cells[row][column] = CellState.THEIRS
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

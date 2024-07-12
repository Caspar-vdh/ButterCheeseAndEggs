package com.dandykong.butter.game

enum class CellState(val state: Int) {
    EMPTY(0),
    MINE(1),
    THEIRS(2);

    companion object {
        fun fromInt(state: Int) = CellState.values().first { it.state == state }
    }
}

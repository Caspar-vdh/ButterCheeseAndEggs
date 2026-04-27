package com.dandykong.butter.shared.ui

import com.dandykong.butter.shared.game.grid.Grid

abstract class GridDrawer<IdType>() {
    abstract fun draw(grid: Grid<IdType>)
    abstract fun waitForUser()
}

package com.dandykong.butter.ui

import com.dandykong.butter.game.grid.Grid

abstract class GridDrawer() {
    abstract fun draw(grid: Grid)
    abstract fun waitForUser()
}

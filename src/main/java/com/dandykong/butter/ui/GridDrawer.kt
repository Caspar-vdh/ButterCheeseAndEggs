package com.dandykong.butter.ui

import com.dandykong.butter.game.Grid

abstract class GridDrawer(val grid: Grid) {
    abstract fun draw()
}

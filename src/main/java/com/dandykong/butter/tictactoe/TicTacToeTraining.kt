package com.dandykong.butter.tictactoe

import com.dandykong.butter.shared.game.grid.Grid
import com.dandykong.butter.shared.game.training.Training
import com.dandykong.butter.shared.game.training.TrainingConfiguration
import com.dandykong.butter.tictactoe.grid.NR_GRID_COLUMNS
import com.dandykong.butter.tictactoe.grid.NR_GRID_ROWS
import com.dandykong.butter.tictactoe.grid.TicTacToeGrid
import com.dandykong.butter.tictactoe.ui.ConsoleDrawer
import com.dandykong.logger.ButterLogger
import com.dandykong.butter.shared.player.CPUPlayer
import com.dandykong.butter.shared.rewardstrategies.RewardStrategy
import com.dandykong.butter.tictactoe.state.TicTacToeGridState
import com.dandykong.butter.tictactoe.state.TicTacToeGridStateFactory
import com.dandykong.butter.tictactoe.state.TicTacToeStateStore
import com.dandykong.butter.tictactoe.state.actionIdToRowAndColumn
import java.io.DataInputStream
import java.io.FileInputStream

const val NR_OF_GAMES = 100000
const val NR_OF_GAMES_FOR_STORE = 200

private const val FILE_PATH = "C:/Users/c_van/Projects/data/ButterCheeseAndEggs/training.dat"

class TicTacToeTraining(
    configuration: TrainingConfiguration,
    players: Array<CPUPlayer<Int, TicTacToeGridState>>,
    positiveRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
    negativeRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
    stateStore: TicTacToeStateStore,
    drawer: ConsoleDrawer?
): Training<Int, TicTacToeGridState>(
    configuration,
    players,
    positiveRewardStrategy,
    negativeRewardStrategy,
    stateStore,
    drawer
) {

    constructor(
        configuration: TrainingConfiguration,
        players: Array<CPUPlayer<Int, TicTacToeGridState>>,
        positiveRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
        negativeRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
        stateStore: TicTacToeStateStore,
        drawer: ConsoleDrawer?,
        log: ButterLogger?
    ) : this(configuration, players, positiveRewardStrategy, negativeRewardStrategy, stateStore, drawer) {
        this.log = log
    }

    override fun createNewGrid(): Grid<Int> {
        return TicTacToeGrid()
    }

    override fun createGridStateFromGrid(
        grid: Grid<Int>,
        id: Int
    ): TicTacToeGridState {
        return TicTacToeGridState.createNewFromGrid(grid as TicTacToeGrid, id)
    }

    override fun updateGridWithAction(
        grid: Grid<Int>,
        action: Int,
        playerId: Int
    ) {
        val (row, column) = actionIdToRowAndColumn(action)
        grid.setCell(row, column, playerId)
    }

    companion object {
        fun create(
            players: Array<CPUPlayer<Int, TicTacToeGridState>>,
            positiveRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
            negativeRewardStrategy: RewardStrategy<Int, TicTacToeGridState>,
            drawer: ConsoleDrawer?,
            log: ButterLogger?
        ) : TicTacToeTraining {
            val configuration = TrainingConfiguration(
                nrOfGames = NR_OF_GAMES,
                nrOfGamesForStore = NR_OF_GAMES_FOR_STORE,
                filePath = FILE_PATH,
            )
            val stateStore = TicTacToeStateStore(
                DataInputStream(FileInputStream(FILE_PATH)),
                NR_GRID_ROWS * NR_GRID_COLUMNS,
                TicTacToeGridStateFactory(),
                log = log,
            )
            return TicTacToeTraining(
                configuration = configuration,
                players = players,
                positiveRewardStrategy = positiveRewardStrategy,
                negativeRewardStrategy = negativeRewardStrategy,
                stateStore = stateStore,
                drawer = drawer,
                log = log
            )
        }
    }
}
package com.dandykong.butter.game

import com.dandykong.butter.game.grid.Grid
import com.dandykong.butter.game.grid.GridStateFactory
import com.dandykong.butter.game.grid.NR_GRID_COLUMNS
import com.dandykong.butter.game.grid.NR_GRID_ROWS
import com.dandykong.training.actionselectionstrategies.SelectHighestStrategy
import com.dandykong.training.basics.StateStore
import com.dandykong.training.player.CPUPlayer
import com.dandykong.training.player.HumanPlayer
import com.dandykong.training.player.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import kotlin.random.Random

private const val DEFAULT_RESOURCE_PATH = "/training.dat"

class GameFacade(resourcePath: String) {

    var gameStateListener: GameStateListener? = null
    var gameGridListener: GameGridListener? = null
    var gameEventListener: GameEventListener? = null


    lateinit var grid: Grid
    private val players: List<Player<GridState>> = listOf(
        HumanPlayer(Player.PLAYER_1),
        CPUPlayer(Player.PLAYER_2, SelectHighestStrategy())
    )
    private var currentPlayer = -1
    private val stateStore: StateStore<GridState>

    init {
        val stream = this::class.java.getResourceAsStream(resourcePath)?.let { DataInputStream(it) }
        // TODO: Error handling?
        stateStore = StateStore(
            stream,
            NR_GRID_ROWS * NR_GRID_COLUMNS,
            GridStateFactory()
        )
    }

    constructor(): this(DEFAULT_RESOURCE_PATH)

    fun startGame() {
        grid = Grid.createInitial()

        currentPlayer = Random.nextInt(2)
        when (val player = players[currentPlayer]) {
            is HumanPlayer -> {
                gameEventListener?.onGameStarted(Player.Type.HUMAN_PLAYER)
                gameStateListener?.onStateChanged(GameState.WAITING_FOR_PLAYER)
            }
            is CPUPlayer -> {
                gameEventListener?.onGameStarted(Player.Type.CPU_PLAYER)
                gameStateListener?.onStateChanged(GameState.PROCESSING_MOVE)
                processCpuMove(player)
            }
        }
    }

    fun processMove(row: Int, column: Int) {
        grid.setCell(row, column, players[currentPlayer].id)
        gameGridListener?.onGridUpdated(grid, row, column)
        if (grid.winningPlayer != null) {
            gameStateListener?.onStateChanged(GameState.IDLE)
            val winner = players.filter { it.id == grid.winningPlayer }[0]
            gameEventListener?.onGameTerminated(winner.type)
        } else if (grid.isFull()) {
            gameStateListener?.onStateChanged(GameState.IDLE)
            gameEventListener?.onGameTerminated(null)
        } else {
            currentPlayer = 1 - currentPlayer
            val newState = when (players[currentPlayer].type) {
                Player.Type.CPU_PLAYER -> GameState.PROCESSING_MOVE
                Player.Type.HUMAN_PLAYER -> GameState.WAITING_FOR_PLAYER
            }
            gameStateListener?.onStateChanged(newState)
            if (newState == GameState.PROCESSING_MOVE) {
                processCpuMove(players[currentPlayer] as CPUPlayer)
            }
        }
    }

    private fun processCpuMove(player: CPUPlayer<GridState>) {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            val id = grid.generateId(player.id)
            val state =
                if (stateStore.hasStateForId(id)) {
                    stateStore.getStateForId(id)!!
                } else {
                    val s = GridState.createNewFromGrid(grid, id)
                    stateStore.addState(s)
                    s
                }
            val nextAction = player.nextAction(state)
            val (row, column) = actionIdToRowAndColumn(nextAction)
            processMove(row, column)
        }
    }
}
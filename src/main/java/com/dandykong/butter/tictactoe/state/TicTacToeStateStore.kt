package com.dandykong.butter.tictactoe.state

import com.dandykong.butter.shared.state.StateStore
import com.dandykong.logger.ButterLogger
import java.io.DataInputStream
import java.io.DataOutputStream

class TicTacToeStateStore(
    inputStream: DataInputStream?,
    nrActionsForState: Int,
    factory: TicTacToeGridStateFactory,
    log: ButterLogger? = null
): StateStore<TicTacToeGridState, Int>(inputStream, nrActionsForState, factory, log) {
    override fun writeId(stream: DataOutputStream, id: Int) {
        stream.writeInt(id)
    }

    override fun readId(stream: DataInputStream): Int {
        return stream.readInt()
    }
}
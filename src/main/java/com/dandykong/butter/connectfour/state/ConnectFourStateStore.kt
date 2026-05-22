package com.dandykong.butter.connectfour.state

import com.dandykong.butter.connectfour.grid.NR_GRID_COLUMNS
import com.dandykong.butter.shared.state.StateStore
import com.dandykong.logger.ButterLogger
import java.io.DataInputStream
import java.io.DataOutputStream

@Suppress("unused")
class ConnectFourStateStore(
    inputStream: DataInputStream?,
    nrActionsForState: Int,
    factory: ConnectFourGridStateFactory,
    log: ButterLogger? = null
): StateStore<ConnectFourGridState, StateId>(inputStream, nrActionsForState, factory, log) {
    override fun writeId(stream: DataOutputStream, id: StateId) {
        stream.write(id.bytes)
    }

    override fun readId(stream: DataInputStream): StateId {
        val byteArray = ByteArray(NR_GRID_COLUMNS)
        val count = stream.read(byteArray)
        require(count == NR_GRID_COLUMNS)
        return StateId(byteArray)
    }
}
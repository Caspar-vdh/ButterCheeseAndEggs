package com.dandykong.training.basics

import com.dandykong.logger.LOG
import java.io.DataInputStream
import java.io.DataOutputStream

@OptIn(ExperimentalUnsignedTypes::class)
class StateStore<S>(
    inputStream: DataInputStream?,
    private val nrActionsForState: Int,
    private val factory: StateFactory<S>
) where S : State {
    private val store: MutableMap<Int, S>

    init {
        store = getNewOrPersistedStore(inputStream)
    }

    fun getStateForId(id: Int): S? {
        return store[id]
    }

    fun hasStateForId(id: Int): Boolean {
        return store.containsKey(id)
    }

    fun addState(state: S) {
        store[state.id] = state
    }

    fun persistStore(stream: DataOutputStream) {
        store.values.forEach {
            stream.writeInt(it.id)
            for (i in 0 until nrActionsForState) {
                stream.writeByte(it.weights[i].toInt())
            }
        }
        LOG.info("Persisted store, wrote ${store.size} states")
        stream.close()
    }

    private fun getNewOrPersistedStore(inputStream: DataInputStream?): MutableMap<Int, S> {
        val tempStore = hashMapOf<Int, S>()
        inputStream?.let {
            while (it.available() > 0) {
                val id = it.readInt()
                val weights = UByteArray(nrActionsForState)
                for (i in 0 until nrActionsForState) {
                    weights[i] = it.readByte().toUByte()
                }
                val state = factory.createNew(id, weights)
                tempStore[state.id] = state
            }
            inputStream.close()
            LOG.info("Read store, ${tempStore.size} states")
        }
        return tempStore
    }

}

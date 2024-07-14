package com.dandykong.training.basics

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.io.path.Path
import kotlin.io.path.exists

@OptIn(ExperimentalUnsignedTypes::class)
class StateStore<S>(
    private val filePath: String,
    private val nrActionsForState: Int,
    private val factory: StateFactory<S>
) where S : State {
    private val store: MutableMap<Int, S>

    init {
        store = getNewOrPersistedStore()
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

    fun persistStore() {
        val stream = DataOutputStream(FileOutputStream(filePath))
        store.values.forEach{
            stream.writeInt(it.id)
            for (i in 0 until nrActionsForState) {
                stream.writeByte(it.weights[i].toInt())
            }
        }
        stream.close()
    }

    private fun getNewOrPersistedStore(): MutableMap<Int, S> {
        val tempStore = hashMapOf<Int, S>()
        if (Path(filePath).exists()) {
            val stream = DataInputStream(FileInputStream(filePath))
            while (stream.available() > 0) {
                val id = stream.readInt()
                val weights = UByteArray(nrActionsForState)
                for (i in 0 until nrActionsForState) {
                    weights[i] = stream.readByte().toUByte()
                }
                val state = factory.createNew(id, weights)
                tempStore[state.id] = state
            }
            stream.close()
        }
        return tempStore
    }

}

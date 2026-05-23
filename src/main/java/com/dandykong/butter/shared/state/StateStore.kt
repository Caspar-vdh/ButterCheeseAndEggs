package com.dandykong.butter.shared.state

import com.dandykong.logger.ButterLogger
import java.io.DataInputStream
import java.io.DataOutputStream

@OptIn(ExperimentalUnsignedTypes::class)
abstract class StateStore<StateType, StateIdType>(
    inputStream: DataInputStream?,
    private val nrActionsForState: Int,
    private val factory: StateFactory<StateType, StateIdType>,
    private val log: ButterLogger?
) where StateType : State<StateIdType> {
    private val store: MutableMap<StateIdType, StateType>

    init {
        store = getNewOrPersistedStore(inputStream)
    }

    fun getStateForId(id: StateIdType): StateType? {
        return store[id]
    }

    fun hasStateForId(id: StateIdType): Boolean {
        return store.containsKey(id)
    }

    fun addState(state: StateType) {
        store[state.id] = state
    }

    abstract fun writeId(stream: DataOutputStream, id: StateIdType)
    abstract fun readId(stream: DataInputStream): StateIdType

    fun persistStore(stream: DataOutputStream) {
        store.values.forEach {
            writeId(stream, it.id)
            for (i in 0 until nrActionsForState) {
                stream.writeByte(it.weights[i].toInt())
            }
        }
        log?.info("Persisted store, wrote ${store.size} states")
    }

    private fun getNewOrPersistedStore(inputStream: DataInputStream?): MutableMap<StateIdType, StateType> {
        val tempStore = hashMapOf<StateIdType, StateType>()
        inputStream?.let {
            while (it.available() > 0) {
                val id = readId(it)
                val weights = UByteArray(nrActionsForState)
                for (i in 0 until nrActionsForState) {
                    weights[i] = it.readByte().toUByte()
                }
                val state = factory.createNew(id, weights)
                tempStore[state.id] = state
            }
            inputStream.close()
            log?.info("Read store, ${tempStore.size} states")
        }
        return tempStore
    }

}

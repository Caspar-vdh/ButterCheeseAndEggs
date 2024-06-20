package com.dandykong.training.basics

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalUnsignedTypes::class)
class StateStore<S>(private val filePath: String) where S : State {
    private val store: MutableMap<Int, S>

    init {
        store = getNewOrPersistedStore(filePath)
    }

    fun getStateForId(id: Int): S? {
        return store[id]
    }

    fun addState(state: S) {
        store[state.id] = state
    }

    fun persistStore() {
        val file = FileOutputStream(filePath)
        store.values.forEach{
            file.write(it.id)
            file.write(it.weights.toByteArray())
        }
        file.flush()
        file.close()
    }

    private fun getNewOrPersistedStore(filePath: String): MutableMap<Int, S> {
//        val file = FileInputStream(filePath)
//        file.read()
        return hashMapOf()
    }

}

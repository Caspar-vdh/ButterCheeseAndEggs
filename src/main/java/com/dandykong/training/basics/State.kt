package com.dandykong.training.basics

abstract class State(val id: Int, val weights: UByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}

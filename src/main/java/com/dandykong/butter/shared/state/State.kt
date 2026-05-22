package com.dandykong.butter.shared.state

@OptIn(ExperimentalUnsignedTypes::class)
abstract class State<IdType>(val id: IdType, val weights: UByteArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State<IdType>

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
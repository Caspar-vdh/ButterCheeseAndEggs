package com.dandykong.butter.connectfour.state

data class StateId(val bytes: ByteArray) : Comparable<StateId> {
    init {
        require(bytes.size == 11) { "StateId must be exactly 11 bytes" }
    }

    override fun compareTo(other: StateId): Int {
        for (i in 0 until 11) {
            val cmp = bytes[i].toUByte().compareTo(other.bytes[i].toUByte())
            if (cmp != 0) return cmp
        }
        return 0
    }

    // ByteArray doesn't implement these by content — you must override
    override fun equals(other: Any?): Boolean =
        other is StateId && bytes.contentEquals(other.bytes)

    override fun hashCode(): Int = bytes.contentHashCode()

    companion object {

    }

}
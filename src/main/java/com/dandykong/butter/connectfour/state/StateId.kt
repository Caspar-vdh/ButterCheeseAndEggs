package com.dandykong.butter.connectfour.state

data class StateId(val bytes: ByteArray) : Comparable<StateId> {
    init {
        require(bytes.size == NR_BYTES) { "StateId must be exactly $NR_BYTES bytes" }
    }

    override fun compareTo(other: StateId): Int {
        for (i in 0 until NR_BYTES) {
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
        // 42 cells at 2 bits each = 84 bits
        const val NR_BYTES = 11
    }

}

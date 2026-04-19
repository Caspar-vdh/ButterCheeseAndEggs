package com.dandykong.logger

interface ButterLogger {
    fun info(var1: String)

    fun info(var1: String, var2: Any)

    fun info(var1: String, var2: Any, var3: Any)

    fun info(var1: String, vararg var2: Any)
}
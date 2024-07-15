package com.dandykong.logger

import ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
class LOG {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ROOT_LOGGER_NAME)

        fun info(var1: String) {
            logger.info(var1)
        }

        fun info(var1: String, var2: Any) {
            logger.info(var1, var2)
        }

        fun info(var1: String, var2: Any, var3: Any) {
            logger.info(var1, var2, var3)
        }

        fun info(var1: String, vararg var2: Any) {
            logger.info(var1, var2)
        }
    }
}
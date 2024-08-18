package com.github.valentina810

import com.github.valentina810.model.Data
import com.sun.org.slf4j.internal.Logger
import com.sun.org.slf4j.internal.LoggerFactory
import jakarta.servlet.http.HttpServletRequest
import java.io.IOException
import java.util.Optional

class Converter {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(Converter::class.java)
    }

    fun convert(request: HttpServletRequest): Data {
        try {
            val cachedRequest = Optional.of(CachedBodyHttpServletRequest(request)).get()
            return Data(cachedRequest, CurlCommandBuilder.buildCurlCommand(cachedRequest))
        } catch (e: IOException) {
            log.error("Ошибка при создании CachedBodyHttpServletRequest: ", e)
            return Data(null, "")
        }
    }
}
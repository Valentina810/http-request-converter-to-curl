package com.github.valentina810

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private val cachedBody: ByteArray

    init {
        this.cachedBody = cacheRequestBody(request)
    }

    @Throws(IOException::class)
    private fun cacheRequestBody(request: HttpServletRequest): ByteArray {
        request.getInputStream().use { requestInputStream ->
            return requestInputStream.readAllBytes()
        }
    }

    val inputStream: ServletInputStream
        get() = CachedBodyServletInputStream(
            this.cachedBody
        )

    val reader: BufferedReader
        get() = BufferedReader(InputStreamReader(ByteArrayInputStream(this.cachedBody)))

    private class CachedBodyServletInputStream(cachedBody: ByteArray?) : ServletInputStream() {
        private val byteArrayInputStream = ByteArrayInputStream(cachedBody)

        val isFinished: Boolean
            get() = byteArrayInputStream.available() == 0

        val isReady: Boolean
            get() = true

        override fun setReadListener(readListener: ReadListener?) {
        }

        override fun read(): Int {
            return byteArrayInputStream.read()
        }

        override fun isFinished(): Boolean {
            TODO("Not yet implemented")
        }

        override fun isReady(): Boolean {
            TODO("Not yet implemented")
        }
    }
}
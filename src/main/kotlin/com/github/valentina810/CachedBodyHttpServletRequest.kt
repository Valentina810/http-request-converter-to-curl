package com.github.valentina810

import com.github.valentina810.exception.CreateCashedBodyException
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

    private fun cacheRequestBody(request: HttpServletRequest): ByteArray {
        try {
            request.inputStream.use { requestInputStream ->
                return requestInputStream.readAllBytes()
            }
        } catch (ex: NullPointerException) {
            throw NullPointerException("Входной запрос не может быть null!")
        } catch (e: IOException) {
            throw CreateCashedBodyException("Ошибка при создании CachedBodyHttpServletRequest: $e")
        }
    }

    override fun getInputStream(): ServletInputStream {
        return CachedBodyServletInputStream(
            this.cachedBody
        )
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(ByteArrayInputStream(this.cachedBody)))
    }

    private class CachedBodyServletInputStream(cachedBody: ByteArray?) : ServletInputStream() {
        private val byteArrayInputStream = ByteArrayInputStream(cachedBody)

        override fun isFinished(): Boolean {
            return byteArrayInputStream.available() == 0
        }

        override fun isReady(): Boolean {
            return true
        }

        override fun setReadListener(readListener: ReadListener) {
        }

        override fun read(): Int {
            return byteArrayInputStream.read()
        }
    }
}
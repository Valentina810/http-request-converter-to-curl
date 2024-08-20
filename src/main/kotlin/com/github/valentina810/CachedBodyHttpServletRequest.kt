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
    /**
     * Поле, которое хранит кешированное тело запроса
     */
    private val cachedBody: ByteArray

    init {
        this.cachedBody = cacheRequestBody(request)
    }

    /**
     * Метод считывает весь входной поток запроса и сохраняет его в виде массива байт (ByteArray)
     */
    private fun cacheRequestBody(request: HttpServletRequest): ByteArray {
        try {
            request.inputStream.use { requestInputStream ->
                return requestInputStream.readAllBytes()
            }
        } catch (ex: NullPointerException) {
            throw NullPointerException("Входной запрос не может быть null!")//#todo перевести сообщения эксепшенов на англ язык
        } catch (e: IOException) {
            throw CreateCashedBodyException("Ошибка при создании CachedBodyHttpServletRequest: $e")
        }
    }

    /**
     * Возвращает новый экземпляр ServletInputStream,
     * который читает из кешированного тела запроса (cachedBody)
     */
    override fun getInputStream(): ServletInputStream {
        return CachedBodyServletInputStream(
            this.cachedBody
        )
    }

    /**
     * Возвращает BufferedReader, который читает текст из кешированного тела запроса,
     * используя InputStreamReader и ByteArrayInputStream
     */
    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(ByteArrayInputStream(this.cachedBody)))
    }

    /**
     * Предоставляет функциональность для чтения данных из кешированного тела запроса
     */
    private class CachedBodyServletInputStream(cachedBody: ByteArray?) : ServletInputStream() {
        /**
         * Поле используется для хранения входного потока байтов
         */
        private val byteArrayInputStream = ByteArrayInputStream(cachedBody)

        /**
         * Возвращает true, если все байты были прочитаны из потока
         */
        override fun isFinished(): Boolean {
            return byteArrayInputStream.available() == 0
        }

        /**
         * Всегда возвращает true, что означает, что поток готов к чтению
         */
        override fun isReady(): Boolean {
            return true
        }

        /**
         * Пустой метод, так как для кешированного тела нет необходимости в асинхронном чтении
         */
        override fun setReadListener(readListener: ReadListener) {
        }

        /**
         * Читает следующий байт из потока
         */
        override fun read(): Int {
            return byteArrayInputStream.read()
        }
    }
}
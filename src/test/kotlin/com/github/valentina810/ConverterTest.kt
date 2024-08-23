package com.github.valentina810

import com.github.valentina810.exception.CreateCashedBodyException
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader

class ConverterTest {

    class ServletInputStreamWrapper(private val inputStream: ByteArrayInputStream) :
        ServletInputStream() {
        override fun isFinished(): Boolean = inputStream.available() == 0
        override fun isReady(): Boolean = true
        override fun setReadListener(readListener: ReadListener?) {}
        override fun read(): Int = inputStream.read()
    }

    @Test
    fun `convert should return valid Data when request is valid`() {
        // Arrange
        val request = mock(HttpServletRequest::class.java)
        val body = "test body"
        val inputStream = ByteArrayInputStream(body.toByteArray())
        val reader = BufferedReader(InputStreamReader(inputStream))

        `when`(request.inputStream).thenReturn(ServletInputStreamWrapper(inputStream))
        `when`(request.reader).thenReturn(reader)
        `when`(request.method).thenReturn("POST")
        `when`(request.requestURL).thenReturn(StringBuffer("http://example.com/test"))
        `when`(request.remoteAddr).thenReturn("127.0.0.1")

        val converter = Converter()

        // Act
        val result = converter.convert(request)

        // Assert
        assertAll(
            { assertNotNull(result.cachedBodyHttpServletRequest) },
            { assertNotNull(result.curl) },
            {
                assertEquals(
                    "curl -X POST 'http://example.com/test' --data 'test body' # IP: 127.0.0.1",
                    result.curl
                )
            }
        )
    }

    @Test
    fun `convert should return valid Data when request with queryString is valid`() {
        // Arrange
        val request = mock(HttpServletRequest::class.java)
        val body = "test body"
        val inputStream = ByteArrayInputStream(body.toByteArray())
        val reader = BufferedReader(InputStreamReader(inputStream))

        `when`(request.inputStream).thenReturn(ServletInputStreamWrapper(inputStream))
        `when`(request.reader).thenReturn(reader)
        `when`(request.method).thenReturn("PUT")
        `when`(request.queryString).thenReturn("parameter1")
        `when`(request.requestURL).thenReturn(StringBuffer("http://example.com/test"))
        `when`(request.remoteAddr).thenReturn("127.0.0.1")

        val converter = Converter()

        // Act
        val result = converter.convert(request)

        // Assert
        assertAll(
            { assertNotNull(result.cachedBodyHttpServletRequest) },
            { assertNotNull(result.curl) },
            {
                assertEquals(
                    "curl -X PUT 'http://example.com/test?parameter1' --data 'test body' # IP: 127.0.0.1",
                    result.curl
                )
            }
        )
    }

    @Test
    fun `convert should return valid Data with headers when request has headers`() {
        // Arrange
        val request = mock(HttpServletRequest::class.java)
        val body = "test body"
        val inputStream = ByteArrayInputStream(body.toByteArray())
        val reader = BufferedReader(InputStreamReader(inputStream))

        val headerNames = listOf("Header1", "Header2")
        val headers = mapOf("Header1" to "Value1", "Header2" to "Value2")

        `when`(request.inputStream).thenReturn(ServletInputStreamWrapper(inputStream))
        `when`(request.reader).thenReturn(reader)
        `when`(request.method).thenReturn("POST")
        `when`(request.requestURL).thenReturn(StringBuffer("http://example.com/test"))
        `when`(request.remoteAddr).thenReturn("127.0.0.1")
        `when`(request.headerNames).thenReturn(java.util.Collections.enumeration(headerNames))
        headers.forEach { (key, value) ->
            `when`(request.getHeader(key)).thenReturn(value)
        }

        val converter = Converter()

        // Act
        val result = converter.convert(request)

        // Assert
        assertAll(
            { assertNotNull(result.cachedBodyHttpServletRequest) },
            { assertNotNull(result.curl) },
            {
                assertEquals(
                    "curl -X POST 'http://example.com/test' -H 'Header1: Value1'  -H 'Header2: Value2' --data 'test body' # IP: 127.0.0.1",
                    result.curl
                )
            }
        )
    }

    @Test
    fun `convert should throw NullPointerException on null request`() {
        // Arrange
        val request = mock(HttpServletRequest::class.java)

        val converter = Converter()

        // Act & Assert
        val exception = assertThrows<NullPointerException> {
            converter.convert(request)
        }
        assertEquals("The input request cannot be null!", exception.message)
    }

    @Test
    fun `convert should throw CreateCashedBodyException when IOException occurs`() {
        // Arrange
        val request = mock(HttpServletRequest::class.java)
        `when`(request.inputStream).thenThrow(IOException::class.java)

        val converter = Converter()

        // Act & Assert
        val exception = assertThrows<CreateCashedBodyException> {
            converter.convert(request)
        }
        assertTrue(exception.message!!.contains("Error creating CachedBodyHttpServletRequest:"))
    }
}

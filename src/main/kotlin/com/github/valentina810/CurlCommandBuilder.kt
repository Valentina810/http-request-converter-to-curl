package com.github.valentina810

import jakarta.servlet.http.HttpServletRequest
import java.io.IOException
import java.util.Collections.list
import java.util.stream.Collectors

object CurlCommandBuilder {
    fun buildCurlCommand(request: HttpServletRequest): String {
        return StringBuilder()
            .append("curl -X ")
            .append(request.method)
            .append(" '")
            .append(request.requestURL)
            .append(getQueryParams(request))
            .append(getHeaders(request))
            .append(getCurlBody(request))
            .append(" # IP: ")
            .append(request.remoteAddr)
            .toString()
    }

    private fun getCurlBody(request: HttpServletRequest): String {
        val curlBody = StringBuilder()
        val data = " --data '"
        try {
            request.reader.use { reader ->
                val body =
                    reader.lines().collect(Collectors.joining(System.lineSeparator()))
                if (body.isNotEmpty()) {
                    curlBody.append(data).append(body).append("'")
                }
            }
        } catch (e: IOException) {
            curlBody.append(data).append("Не удалось получить тело запроса").append("'")
        }
        return curlBody.toString()
    }

    private fun getHeaders(request: HttpServletRequest): String {
        return request.headerNames?.let { headerNames ->
            list(headerNames)
                .joinToString(" ") { headerName ->
                    " -H '$headerName: ${request.getHeader(headerName)}'"
                }
        } ?: ""
    }

    private fun getQueryParams(request: HttpServletRequest): String {
        val queryString = request.queryString
        return if ((queryString != null)) "?$queryString'" else "'"
    }
}
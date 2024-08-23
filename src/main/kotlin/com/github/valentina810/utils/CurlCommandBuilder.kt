package com.github.valentina810.utils

import jakarta.servlet.http.HttpServletRequest
import java.io.IOException
import java.util.Collections.list
import java.util.stream.Collectors

/**
 * The `CurlCommandBuilder` class is a utility class that helps in constructing
 * a cURL command based on an incoming `HttpServletRequest`. This cURL command
 * includes the HTTP method, URL, query parameters, headers, and the body of the request.
 *
 * This class is used internally to facilitate the conversion of HTTP requests
 * into cURL commands, which can be useful for debugging or logging purposes.
 */
internal object CurlCommandBuilder {

    /**
     * Builds a cURL command representation of the given `HttpServletRequest`.
     *
     * @param request The incoming `HttpServletRequest` to be converted into a cURL command.
     * @return A `String` representing the cURL command that simulates the HTTP request.
     */
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

    /**
     * Retrieves the body of the HTTP request and formats it for inclusion in the cURL command.
     *
     * @param request The incoming `HttpServletRequest` from which the body is extracted.
     * @return A `String` representing the body of the request formatted for a cURL command.
     */
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
            curlBody.append(data).append("Failed to retrieve request body").append("'")
        }
        return curlBody.toString()
    }

    /**
     * Retrieves the headers of the HTTP request and formats them for inclusion in the cURL command.
     *
     * @param request The incoming `HttpServletRequest` from which the headers are extracted.
     * @return A `String` representing the headers of the request formatted for a cURL command.
     */
    private fun getHeaders(request: HttpServletRequest): String {
        return request.headerNames?.let { headerNames ->
            list(headerNames)
                .joinToString(" ") { headerName ->
                    " -H '$headerName: ${request.getHeader(headerName)}'"
                }
        } ?: ""
    }

    /**
     * Retrieves the query parameters of the HTTP request and formats them for inclusion in the cURL command.
     *
     * @param request The incoming `HttpServletRequest` from which the query parameters are extracted.
     * @return A `String` representing the query parameters of the request formatted for a cURL command.
     */
    private fun getQueryParams(request: HttpServletRequest): String {
        val queryString = request.queryString
        return if (queryString != null) "?$queryString'" else "'"
    }
}
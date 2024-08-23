package com.github.valentina810

import com.github.valentina810.exception.CreateCashedBodyException
import com.github.valentina810.utils.CachedBodyHttpServletRequest
import com.github.valentina810.utils.Data
import jakarta.servlet.http.HttpServletRequest

/**
 * The `Converter` class provides functionality to convert an incoming
 * `HttpServletRequest` into a cURL command format and caches the request body.
 *
 * This class is designed to handle the conversion and caching process
 * efficiently, returning a `Data` object containing the cached request
 * and its corresponding cURL representation.
 *
 * Usage example:
 * ```
 * val data = Converter().convert(request)
 * println(data.curl)
 * ```
 *
 * @see com.github.valentina810.utils.CachedBodyHttpServletRequest
 * @see com.github.valentina810.utils.Data
 */
class Converter {

    /**
     * Converts an incoming `HttpServletRequest` into a `Data` object that contains
     * a cached version of the request and a cURL command representation of the request.
     *
     * @param request The incoming `HttpServletRequest` to be converted. It must not be null.
     * @return A `Data` object containing the cached request and its cURL representation.
     * @throws NullPointerException If the provided request is null.
     * @throws CreateCashedBodyException If an error occurs during the creation of the
     *         `CachedBodyHttpServletRequest`.
     */
    fun convert(request: HttpServletRequest): Data {
        val cachedRequest = CachedBodyHttpServletRequest(request)
        return Data(cachedRequest, cachedRequest.getCurl())
    }
}
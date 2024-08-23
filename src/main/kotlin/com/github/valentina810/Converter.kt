package com.github.valentina810

import com.github.valentina810.utils.CachedBodyHttpServletRequest
import com.github.valentina810.utils.Data
import jakarta.servlet.http.HttpServletRequest

class Converter {

    fun convert(request: HttpServletRequest): Data {
        val cachedRequest = CachedBodyHttpServletRequest(request)
        return Data(cachedRequest, cachedRequest.getCurl())
    }
}
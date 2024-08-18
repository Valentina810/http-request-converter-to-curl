package com.github.valentina810.model

import com.github.valentina810.CachedBodyHttpServletRequest

data class Data(
    val cachedBodyHttpServletRequest: CachedBodyHttpServletRequest?,
    val curl: String
)

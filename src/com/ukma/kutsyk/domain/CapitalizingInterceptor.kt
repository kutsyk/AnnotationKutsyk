package com.ukma.kutsyk.domain;

class CapitalizingInterceptor: Interceptor {
    override fun interceptOutputString(interceptedString: String?): String
    = interceptedString?.toUpperCase() ?: ""
}

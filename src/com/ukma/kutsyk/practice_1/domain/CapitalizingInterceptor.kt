package com.ukma.kutsyk.practice_1.domain;

class CapitalizingInterceptor: Interceptor {
    override fun interceptOutputString(interceptedString: String?): String
    = interceptedString?.toUpperCase() ?: ""
}

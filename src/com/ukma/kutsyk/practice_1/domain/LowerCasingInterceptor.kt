package com.ukma.kutsyk.practice_1.domain;

class LowerCasingInterceptor: Interceptor {
    override fun interceptOutputString(interceptedString: String?) : String
            = interceptedString?.toLowerCase() ?: ""
}

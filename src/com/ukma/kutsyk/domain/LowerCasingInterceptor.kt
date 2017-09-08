package com.ukma.kutsyk.domain;

class LowerCasingInterceptor: Interceptor {
    override fun interceptOutputString(interceptedString: String) : String
            = interceptedString.toLowerCase()
}

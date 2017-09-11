package com.ukma.kutsyk.domain

interface Interceptor {
    public fun interceptOutputString(interceptedString: String?): String
}
package com.ukma.kutsyk.practice_1.domain

interface Interceptor {
    public fun interceptOutputString(interceptedString: String?): String
}
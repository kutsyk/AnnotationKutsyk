package com.ukma.kutsyk.practice_1.domain.transport

class Bus : Transport {
    var message: String = ""

    init {
        message = "I am the Bus!"
    }

    override fun transport() {
        println(message)
    }
}

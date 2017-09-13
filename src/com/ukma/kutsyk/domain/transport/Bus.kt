package com.ukma.kutsyk.domain.transport

class Bus : Transport {
    var message: String = ""

    init {
        message = "I am the Bus!"
    }

    override fun transport() {
        println(message)
    }
}

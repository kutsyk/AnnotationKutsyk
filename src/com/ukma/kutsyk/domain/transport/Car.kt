package com.ukma.kutsyk.domain.transport

class Car : Transport {

    var name: String? = ""
    var wheelCount: Int? = 0

    constructor(name: String?, wheelCount: Int) {
        this.name = name
        this.wheelCount = wheelCount
    }

    constructor(name: String?, wheelCount: String) {
        this.name = name
        this.wheelCount = Integer.decode(wheelCount)
    }

    override fun toString(): String {
        val res = StringBuffer()
        res.append(name!! + ": ")
        res.append(wheelCount)
        return res.toString()
    }

    override fun transport() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

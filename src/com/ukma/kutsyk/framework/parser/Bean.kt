package com.ukma.kutsyk.framework.parser

class Bean {
    var name: String = ""
    var className: String = ""
    var constructorArg = ArrayList<String>()
    var properties = ArrayList<Property>()

    override fun toString(): String {
        return name + " : " + className.toString() + constructorArg.toString() + ", " + properties.toString()
    }
}

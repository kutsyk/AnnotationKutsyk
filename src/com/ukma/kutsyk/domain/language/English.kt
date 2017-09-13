package com.ukma.kutsyk.domain.language

class English: Language {

    var propertyForExample = "example"

    override fun sayHello() {
        println("Hello $propertyForExample")
    }
}
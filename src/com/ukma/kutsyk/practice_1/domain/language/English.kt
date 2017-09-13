package com.ukma.kutsyk.practice_1.domain.language

class English: Language {

    var propertyForExample = "example"

    override fun sayHello() {
        println("Hello $propertyForExample")
    }
}
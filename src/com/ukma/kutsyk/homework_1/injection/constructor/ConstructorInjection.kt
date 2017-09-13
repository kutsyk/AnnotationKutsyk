package com.ukma.kutsyk.homework_1.injection.constructor

import com.ukma.kutsyk.homework_1.injection.domain.Client
import com.ukma.kutsyk.homework_1.injection.domain.Order
import com.ukma.kutsyk.homework_1.injection.domain.Product

/*
If I get this(https://dzone.com/articles/constructor-injection-vs-0) right,
 then this should be correct.
 */
fun main(args: Array<String>) {
    val client = Client("Vasyl")
    val product = Product("Raspberry")
    val order = Order(product, client)
    println("Client: $client\n Product: $product\n Order: $order")
}
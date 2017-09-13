package com.ukma.kutsyk.homework_1.injection.domain


/*
Here @product and @client can't be null.
It is checked in compiling time.
*/
data class Order(val product: Product, val client: Client)
package com.ukma.kutsyk.homework_1.injection.setter

import com.ukma.kutsyk.framework.core.GenericXmlApplicationContext

fun main(args: Array<String>) {
    val context = GenericXmlApplicationContext(
            GenericXmlApplicationContext::class.java.getResource("/homework_1.xml").path)
    println(context.getBeanFactory()?.bean("client"))
    println(context.getBeanFactory()?.bean("product"))
    println(context.getBeanFactory()?.bean("order"))
}
package com.ukma.kutsyk.homework_1.injection.`interface`

import com.ukma.kutsyk.framework.core.GenericXmlApplicationContext
import com.ukma.kutsyk.framework.core.ParserTypes
import com.ukma.kutsyk.practice_1.Main
import com.ukma.kutsyk.practice_1.domain.transport.Transport

private val Context  = GenericXmlApplicationContext(Main::class.java)

fun main(args: Array<String>) {
    Context.load(Main::class.java!!.getResource("/Practice_1_conf.xml").getPath())
    Context.setValidating(true)
    Context.setParserType(ParserTypes.SAX)

    val factory = Context.getBeanFactory()
    val bus = factory?.bean("bus", Transport::class.java)
    bus?.transport()

    val car = factory?.bean("car", Transport::class.java)
    car?.transport()

}
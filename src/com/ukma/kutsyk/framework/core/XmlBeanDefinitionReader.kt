package com.ukma.kutsyk.framework.core

import com.ukma.kutsyk.framework.parser.Bean
import com.ukma.kutsyk.framework.parser.ConfigurationParserImpl

enum class ParserTypes {
    DOM, SAX, StAX
}

class XmlBeanDefinitionReader {

    public var beanList: List<Bean>? = null
    public var interceptorList: List<Bean>? = null
    public var parserType: ParserTypes? = null
    public var validating: Boolean = false

    constructor() {
        parserType = ParserTypes.SAX
    }

    fun loadBeanDefinitions(fileName: String) {
        when (parserType) {
            ParserTypes.SAX -> {
                ConfigurationParserImpl(fileName).apply {
                    beanList = beanList()
                    interceptorList = interceptorList()
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}
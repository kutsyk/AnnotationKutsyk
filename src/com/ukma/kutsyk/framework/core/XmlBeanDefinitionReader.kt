package com.ukma.kutsyk.framework.core

import com.ukma.kutsyk.framework.parser.Bean
import com.ukma.kutsyk.framework.parser.ConfigurationParserImpl

class XmlBeanDefinitionReader {

    companion object {
        enum class ParserTypes {
            DOM, SAX, StAX
        }
    }

    public var beanList: List<Bean>? = null
    public var interceptorList: List<Bean>? = null
    public var parserType: ParserTypes? = null
    public var validating: Boolean = false

    constructor()
    {
        parserType = ParserTypes.SAX
    }

    fun loadBeanDefinitions(fileName: String) {
        when (parserType) {
            XmlBeanDefinitionReader.Companion.ParserTypes.SAX -> {
                ConfigurationParserImpl(fileName).apply {
                    beanList = beanList()
                    interceptorList = interceptorList()
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}
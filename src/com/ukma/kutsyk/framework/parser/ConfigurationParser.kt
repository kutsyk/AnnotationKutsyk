package com.ukma.kutsyk.framework.parser

interface ConfigurationParser {
    fun beanList(): List<Bean>
    fun interceptorList(): List<Bean>
}
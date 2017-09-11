package com.ukma.kutsyk.framework.parser

import com.ukma.kutsyk.framework.ConfigurationConstants
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class ConfigurationParserImpl(ConfigurationFile: String = "",
                              private val __ResultList: ArrayList<Bean> = arrayListOf<Bean>(),
                              private val __InterceptorList: ArrayList<Bean> = arrayListOf<Bean>()) : ConfigurationParser {

    init {
        if (Files.exists(Paths.get(ConfigurationFile.trimStart('/')))) {
            val jsoupDoc = Jsoup.parse(
                    File(ConfigurationFile).readText(),
                    "",
                    Parser.xmlParser())
            jsoupDoc
                    .select("${ConfigurationConstants.BEAN}")
                    .forEach { xmlBean ->
                        val tmpBean = Bean()
                        tmpBean.name = xmlBean.attr(ConfigurationConstants.ATTRIBUTES.ID)
                        tmpBean.className = xmlBean.attr(ConfigurationConstants.ATTRIBUTES.CLASS)

                        xmlBean.children().select(ConfigurationConstants.CONSTRUCTOR_ARGS).forEach { consArgs ->
                            tmpBean.constructorArg.add(consArgs.attr(ConfigurationConstants.ATTRIBUTES.TYPE))
                            tmpBean.constructorArg.add(consArgs.attr(ConfigurationConstants.ATTRIBUTES.VALUE))
                        }

                        tmpBean.properties.addAll(
                                xmlBean.children().select(ConfigurationConstants.PROPERTY).flatMap
                                { properties ->
                                    listOf(
                                            properties.attr(ConfigurationConstants.ATTRIBUTES.NAME),
                                            properties.attr(ConfigurationConstants.ATTRIBUTES.VALUE)
                                    )
                                }
                        )
                        __ResultList.add(tmpBean)
                    }

            jsoupDoc
                    .select("${ConfigurationConstants.INTERCEPTOR}")
                    .forEach { xmlBean ->
                        val tmpBean = Bean()
                        tmpBean.name = xmlBean.attr(ConfigurationConstants.ATTRIBUTES.ID)
                        tmpBean.className = xmlBean.attr(ConfigurationConstants.ATTRIBUTES.CLASS)

                        xmlBean.children().select(ConfigurationConstants.CONSTRUCTOR_ARGS).forEach { consArgs ->
                            tmpBean.constructorArg.add(consArgs.attr(ConfigurationConstants.ATTRIBUTES.TYPE))
                            tmpBean.constructorArg.add(consArgs.attr(ConfigurationConstants.ATTRIBUTES.VALUE))
                        }

                        tmpBean.properties.addAll(
                                xmlBean.children().select(ConfigurationConstants.PROPERTY).flatMap
                                { properties ->
                                    listOf(
                                            properties.attr(ConfigurationConstants.ATTRIBUTES.NAME),
                                            properties.attr(ConfigurationConstants.ATTRIBUTES.VALUE)
                                    )
                                }
                        )
                        __InterceptorList.add(tmpBean)
                    }
        }
    }

    override fun beanList(): List<Bean> = __ResultList
    override fun interceptorList(): List<Bean> = __InterceptorList
}
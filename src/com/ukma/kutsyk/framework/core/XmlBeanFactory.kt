package com.ukma.kutsyk.framework.core

import com.ukma.kutsyk.framework.parser.Bean
import java.lang.reflect.Constructor
import java.util.HashMap

class XmlBeanFactory(xmlFilePath: String, xbdr: XmlBeanDefinitionReader) : IBeanFactory {

    internal var beanTable = HashMap<String, Any>()
    internal var interceptorTable = HashMap<String, Any>()

    private fun generateBeans(beanList: List<Bean>) {
        beanList.forEach { bean ->
            try {
                val clazz = Class.forName(bean.className)
                val constructor: Constructor<*>
                val obj: Any
                val constArgs = bean.constructorArg

                if (constArgs.isNotEmpty())
                {
                    val consClasses = arrayOfNulls<Class<*>>(constArgs.size / 2)
                    var j = 0
                    for (i in 0 until constArgs.size step 2) {
                        consClasses[j] =
                                if (constArgs[i].contentEquals("String")
                                        || constArgs[i].isEmpty()) {
                                    String::class.java
                                } else if (classLibrary.containsKey(constArgs[i])) {
                                    primitiveClassForName(constArgs[i])
                                } else {
                                    Class.forName(constArgs[i])
                                }
                        j++
                    }

                    constructor = clazz.getConstructor(*consClasses)

                    val conArgs = arrayOfNulls<Any>(consClasses.size)
                    j = 0
                    for (i in 1 until constArgs.size step 2) {
                        conArgs[j] =
                                if (consClasses[j]!!.isPrimitive)
                                {
                                    wrapperClassValueForPrimitiveType(consClasses[j]!!, constArgs[i])
                                }
                                else
                                {
                                    consClasses[j]!!.cast(constArgs[i])
                                }
                        j++
                    }
                    obj = constructor.newInstance(*conArgs)

                }
                else
                {
                    constructor = clazz.getConstructor()
                    obj = constructor.newInstance()
                }

                bean.properties.forEach { prop ->
                    val methodName = "set${prop.name.capitalize()}"
                    val method = obj.javaClass.getMethod(methodName, prop.value.javaClass)
                    method.invoke(obj, prop.value)
                }
                beanTable.put(bean.name, obj)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun setupInterceptors(interceptorList: List<Bean>) {
        for (b in interceptorList) {
            try {
                val clazz = Class.forName(b.className)
                val interceptor = clazz.getConstructor().newInstance()
                interceptorTable.put(b.name, interceptor)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    init {
        xbdr.loadBeanDefinitions(xmlFilePath)
        generateBeans(xbdr.beanList!!)
        setupInterceptors(xbdr.interceptorList!!)
    }

    override fun bean(string: String): Any =
            beanTable[string]!!

    override fun <T> bean(string: String, type: Class<T>): T =
            beanTable[string] as T

    override fun interceptors(): Array<Any> =
            interceptorTable.values.toTypedArray()


}
package com.ukma.kutsyk.framework.core

import com.ukma.kutsyk.framework.parser.Bean
import java.lang.reflect.Constructor
import java.util.HashMap

class XmlBeanFactory: IBeanFactory {
    
    internal var beanTable = HashMap<String, Any>()
    internal var interceptorTable = HashMap<String, Any>()

    constructor(xmlFilePath: String, xbdr: XmlBeanDefinitionReader) {
        xbdr.loadBeanDefinitions(xmlFilePath)
        generateBeans(xbdr.beanList!!)
        setupInterceptors(xbdr.interceptorList!!)
    }

    private fun generateBeans(beanList: List<Bean>) {
        for (b in beanList) {
            try {
                val clazz = Class.forName(b.className)
                val ctor: Constructor<*>
                val obj: Any

                val ca = b.constructorArg

                if (!ca.isEmpty()) {
                    val consClasses = arrayOfNulls<Class<*>>(ca.size / 2)

                    run {
                        var i = 0
                        var j = 0
                        while (i < ca.size) {
                            if (ca.get(i) == null || ca.get(i).contentEquals("String")) {
                                consClasses[j] = String::class.java
                            } else if (classLibrary.containsKey(ca.get(i))) {
                                consClasses[j] = primitiveClassForName(ca.get(i))
                            } else {
                                consClasses[j] = Class.forName(ca.get(i))
                            }
                            j++
                            i += 2
                        }
                    }
                    ctor = clazz.getConstructor(*consClasses)
                    val consArgs = arrayOfNulls<Any>(consClasses.size)
                    var i = 1
                    var j = 0
                    while (i < ca.size) {
                        if (consClasses[j]!!.isPrimitive()) {
                            consArgs[j] = wrapperClassValueForPrimitiveType(consClasses[j]!!, ca.get(i))
                        } else {
                            consArgs[j] = consClasses[j]!!.cast(ca.get(i))
                        }
                        j++
                        i += 2
                    }
                    obj = ctor.newInstance(*consArgs)
                } else {
                    ctor = clazz.getConstructor()
                    obj = ctor.newInstance()
                }

                val props = b.properties

                if (!props.isEmpty()) {
                    var i = 0
                    while (i < props.size) {
                        val first = Character.toUpperCase(props.get(i).get(0))
                        val methodName = "set" + first + props.get(i).substring(1)
                        val method = obj.javaClass.getMethod(methodName,
                                *arrayOf(props.get(i + 1).javaClass))
                        method.invoke(obj, props.get(i + 1))
                        i++
                        i++
                    }
                }

                beanTable.put(b.name, obj)
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

    override fun bean(string: String): Any =
        beanTable[string]!!

    override fun <T> bean(string: String, type: Class<T>): T =
        beanTable[string] as T

    override fun interceptors(): Array<Any> =
            interceptorTable.values.toTypedArray()
}
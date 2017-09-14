package com.ukma.kutsyk.framework.core

import java.lang.reflect.Field

class GenericXmlApplicationContext(xmlFileLocation: String) {

    private companion object {
        val CONFIG_FILE_NAME = GenericXmlApplicationContext::class.java.getResource("/Practice_1_conf.xml").path
    }

    private class ConfigurationException(e: String) : RuntimeException(e) {
        companion object {
            private val serialVersionUID = -2684645760336447485L
        }
    }

    private fun canInstantiate(classToInstantiate: Class<*>, testClass: Class<*>?) =
            !testClass!!.isInterface() && classToInstantiate.isAssignableFrom(testClass)

    private fun isTheSameClassAs(anotherClass: Class<*>, testClass: Class<*>?) =
            anotherClass.name == testClass?.name

    private var reader: XmlBeanDefinitionReader? = null
    private var beanFactory: IBeanFactory? = null

    fun getReader(): XmlBeanDefinitionReader {
        return reader!!
    }

    private var xmlFileLocation: String? = null

    constructor() : this(CONFIG_FILE_NAME) {
    }

    constructor(classObject: Class<*>) : this(CONFIG_FILE_NAME)
    {
        ProcessFields(classObject)
        ProcessMethods(classObject)
    }

    private fun ProcessFields(classObject: Class<*>)
    {
        val fields = classObject.declaredFields
        for (currentField in fields) {
            if (currentField.isAnnotationPresent(Autowiring::class.java)) {
                val myCL = Thread.currentThread().contextClassLoader
                var classLoaderClassesField: Field? = null
                var myCLClass: Class<*> = myCL.javaClass

                while (myCLClass != java.lang.ClassLoader::class.java) {
                    myCLClass = myCLClass.superclass
                }

                try {
                    classLoaderClassesField = myCLClass.getDeclaredField("classes")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                classLoaderClassesField!!.isAccessible = true

                var classes: List<Class<*>>? = null
                try {
                    classes = classLoaderClassesField.get(myCL) as List<Class<*>>
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                val currentFieldClass = currentField.type
                var match: Class<*>? = null

                if (currentField.getAnnotation(Autowiring::class.java).value.isNotEmpty()) {
                    var classInAnnotation: Class<*>? = null
                    try {
                        classInAnnotation = Class.forName(currentField.getAnnotation(Autowiring::class.java)
                                .value)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }

                    if (canInstantiate(currentFieldClass, classInAnnotation)) {
                        match = classInAnnotation
                    } else {
                        throw ConfigurationException("Class specified in annotation is not compatible with "
                                + currentFieldClass.name + ".")
                    }

                } else {
                    if (classes!!.none { it -> canInstantiate(currentFieldClass, it) }) {
                        throw ConfigurationException("No suitable implementation for "
                                + currentFieldClass.name + " found. Please check your configuration file.")
                    }

                    match = classes!!.filter { it -> canInstantiate(currentFieldClass, it) }.first()

                    if (classes!!.any { it ->
                        canInstantiate(currentFieldClass, it)
                                && !isTheSameClassAs(match!!, it)
                    }) {
                        throw ConfigurationException("Ambiguous configuration for "
                                + currentFieldClass.name + ". Please check your configuration file.")
                    }
                }

                try {
                    currentField.isAccessible = true
                    currentField.set(null, match?.newInstance())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun ProcessMethods(classObject: Class<*>)
    {
        val methods = classObject.declaredMethods
        for (currentMethod in methods) {
            if (currentMethod.isAnnotationPresent(Required::class.java)) {
                val myCL = Thread.currentThread().contextClassLoader
                var classLoaderClassesField: Field? = null
                var myCLClass: Class<*> = myCL.javaClass

                while (myCLClass != java.lang.ClassLoader::class.java) {
                    myCLClass = myCLClass.superclass
                }

                try {
                    classLoaderClassesField = myCLClass.getDeclaredField("classes")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                classLoaderClassesField!!.isAccessible = true

                var classes: List<Class<*>>? = null
                try {
                    classes = classLoaderClassesField.get(myCL) as List<Class<*>>
                } catch (e: Exception) {
                    e.printStackTrace()
                }

             /*   val currentFieldClass = currentField.type
                var match: Class<*>? = null

                if (currentField.getAnnotation(Autowiring::class.java).value.isNotEmpty()) {
                    var classInAnnotation: Class<*>? = null
                    try {
                        classInAnnotation = Class.forName(currentField.getAnnotation(Autowiring::class.java)
                                .value)
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }

                    if (canInstantiate(currentFieldClass, classInAnnotation)) {
                        match = classInAnnotation
                    } else {
                        throw ConfigurationException("Class specified in annotation is not compatible with "
                                + currentFieldClass.name + ".")
                    }

                } else {
                    if (!classes!!.any { it -> canInstantiate(currentFieldClass, it) }) {
                        throw ConfigurationException("No suitable implementation for "
                                + currentFieldClass.name + " found. Please check your configuration file.")
                    }

                    match = classes!!.filter { it -> canInstantiate(currentFieldClass, it) }.first()

                    if (classes!!.any { it ->
                        canInstantiate(currentFieldClass, it)
                                && !isTheSameClassAs(match!!, it)
                    }) {
                        throw ConfigurationException("Ambiguous configuration for "
                                + currentFieldClass.name + ". Please check your configuration file.")
                    }
                }

                try {
                    currentField.isAccessible = true
                    currentField.set(null, match?.newInstance())
                } catch (e: Exception) {
                    e.printStackTrace()
                }*/
            }
        }
    }

    init {
        this.xmlFileLocation = xmlFileLocation
        reader = XmlBeanDefinitionReader()
        beanFactory = XmlBeanFactory(this.xmlFileLocation!!, reader!!)
    }

    fun setValidating(validating: Boolean) {
        reader?.validating = validating
    }

    fun setParserType(parserType: ParserTypes) {
        reader?.parserType = parserType
    }

    fun load(xmlFileLocation: String) {
        this.xmlFileLocation = xmlFileLocation
    }

    fun getBeanFactory(): IBeanFactory? {
        return beanFactory
    }

}
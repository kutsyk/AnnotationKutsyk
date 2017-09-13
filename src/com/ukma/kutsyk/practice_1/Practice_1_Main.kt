package com.ukma.kutsyk.practice_1

import com.ukma.kutsyk.practice_1.domain.*
import com.ukma.kutsyk.practice_1.domain.language.English
import com.ukma.kutsyk.practice_1.domain.language.French
import com.ukma.kutsyk.practice_1.domain.language.Language
import com.ukma.kutsyk.practice_1.domain.transport.Bus
import com.ukma.kutsyk.practice_1.domain.transport.Car
import com.ukma.kutsyk.practice_1.domain.transport.Transport
import com.ukma.kutsyk.framework.core.Autowiring
import com.ukma.kutsyk.framework.core.GenericXmlApplicationContext
import com.ukma.kutsyk.framework.core.ParserTypes
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

class Main {

    companion object {
        private val context = GenericXmlApplicationContext(Main::class.java)

        @Autowiring("com.ukma.kutsyk.practice_1.domain.LowerCasingInterceptor")
        var activeInterceptor: Interceptor? = null
    }

    private class ObjectInfo internal constructor(o: Any, name: String?) {

        private var name = "Anonymous"
        private val className: String
        private val superClassName: String
        private val implementedInterfaces: Array<Class<*>>
        private val fields: Array<Field>
        private val constructors: Array<Constructor<*>>
        private val methods: Array<Method>

        init {
            if (name != null && !name.isEmpty()) {
                this.name = name
            }

            this.className = o.javaClass.name
            this.superClassName = o.javaClass.superclass.name
            this.implementedInterfaces = o.javaClass.interfaces
            this.fields = o.javaClass.declaredFields
            this.constructors = o.javaClass.declaredConstructors
            this.methods = o.javaClass.methods
        }

        override fun toString(): String {
            val objectInfo = StringBuilder()
            objectInfo.append("Inspecting $name:\n")
            objectInfo.append("Class name: " + className + "\n")
            objectInfo.append("Parent class: " + superClassName + "\n")
            objectInfo.append("Implemented interfaces:")
            if (implementedInterfaces.size == 0) {
                objectInfo.append(" none\n")
            } else {
                objectInfo.append("\n")
                for (ii in implementedInterfaces) {
                    objectInfo.append("\t* " + ii.name + "\n")
                }
            }

            objectInfo.append("Attributes:\n")
            for (f in fields) {
                val fieldModifiers = f.modifiers
                objectInfo.append("\t* ")

                if (Modifier.isPublic(fieldModifiers)) {
                    objectInfo.append("public ")
                }
                if (Modifier.isProtected(fieldModifiers)) {
                    objectInfo.append("protected ")
                }
                if (Modifier.isPrivate(fieldModifiers)) {
                    objectInfo.append("private ")
                }
                if (Modifier.isFinal(fieldModifiers)) {
                    objectInfo.append("final ")
                }
                if (Modifier.isStatic(fieldModifiers)) {
                    objectInfo.append("static ")
                }
                if (Modifier.isSynchronized(fieldModifiers)) {
                    objectInfo.append("synchronized ")
                }

                objectInfo.append(f.type.simpleName + " " + f.name + ";\n")
            }

            objectInfo.append("Constructors:\n")
            for (c in constructors) {
                val constructorModifiers = c.modifiers
                objectInfo.append("\t* ")

                if (Modifier.isPublic(constructorModifiers)) {
                    objectInfo.append("public ")
                }
                if (Modifier.isProtected(constructorModifiers)) {
                    objectInfo.append("protected ")
                }
                if (Modifier.isPrivate(constructorModifiers)) {
                    objectInfo.append("private ")
                }

                objectInfo.append(c.name + "(")
                if (c.parameterCount > 0) {
                    for (p in c.parameters) {
                        if (Modifier.isFinal(p.modifiers)) {
                            objectInfo.append("final ")
                        }
                        objectInfo.append(p.type.simpleName)

                        if (p.isVarArgs) {
                            objectInfo.delete(objectInfo.length - 2, objectInfo.length).append("...")
                        }
                        objectInfo.append(" " + p.name + ", ")
                    }
                    objectInfo.delete(objectInfo.length - 2, objectInfo.length)
                }
                objectInfo.append(");\n")
            }

            objectInfo.append("Methods:\n")
            for (m in methods) {
                val methodModifiers = m.modifiers
                objectInfo.append("\t* ")

                if (Modifier.isPublic(methodModifiers)) {
                    objectInfo.append("public ")
                }
                if (Modifier.isProtected(methodModifiers)) {
                    objectInfo.append("protected ")
                }
                if (Modifier.isPrivate(methodModifiers)) {
                    objectInfo.append("private ")
                }
                if (Modifier.isFinal(methodModifiers)) {
                    objectInfo.append("final ")
                }
                if (Modifier.isAbstract(methodModifiers)) {
                    objectInfo.append("abstract ")
                }
                if (Modifier.isStatic(methodModifiers)) {
                    objectInfo.append("static ")
                }
                if (Modifier.isSynchronized(methodModifiers)) {
                    objectInfo.append("synchronized ")
                }

                objectInfo.append(m.returnType.simpleName + " " + m.name + "(")
                if (m.parameterCount > 0) {
                    for (p in m.parameters) {
                        if (Modifier.isFinal(p.modifiers)) {
                            objectInfo.append("final ")
                        }
                        objectInfo.append(p.type.simpleName)

                        if (p.isVarArgs) {
                            objectInfo.delete(objectInfo.length - 2, objectInfo.length).append("...")
                        }
                        objectInfo.append(" " + p.name + ", ")
                    }
                    objectInfo.delete(objectInfo.length - 2, objectInfo.length)
                }
                objectInfo.append(");\n")
            }

            return objectInfo.toString()
        }
    }

    fun main(args: Array<String>) {
        context.load(Main::class.java!!.getResource("/Practice_1_conf.xml").getPath())
        context.setValidating(true)
        context.setParserType(ParserTypes.SAX)

        val factory = context.getBeanFactory()

        val greetingService = factory?.bean("greetingService", GreetingService::class.java) as GreetingService
        println(greetingService.getMessage())

        val bus = factory?.bean("bus", Transport::class.java) as Bus
        bus.transport()

        val bus2 = factory?.bean("bus2", Transport::class.java) as Bus
        bus2.transport()

        val car = factory?.bean("car", Transport::class.java) as Car
        println(car.toString())

        println()
        println("================ REFLECTION API DEMO ================")
        println()

        println(ObjectInfo(bus, "bus").toString())
        println("==========")
        println(ObjectInfo(bus.toString(),
                "String representation of the 'bus' object").toString())

        val output = "TeST InTercepTor"
        println("Unintercepted string: " + output)
        println("Intercepted string: " + activeInterceptor?.interceptOutputString(output))

        // My classes test
        val lang1 = factory?.bean("language_1", Language::class.java) as English
        lang1.sayHello()

        val lang2 = factory?.bean("language_2", Language::class.java) as French
        lang2.sayHello()
        println()

        println("Press any key to exit...")
        readLine()
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}
package com.ukma.kutsyk

import com.ukma.kutsyk.domain.*
import com.ukma.kutsyk.framework.core.Autowiring
import com.ukma.kutsyk.framework.core.GenericXmlApplicationContext
import com.ukma.kutsyk.framework.core.GenericXmlApplicationContextJava
import com.ukma.kutsyk.framework.core.ParserTypes
import java.io.IOException
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

class Main {

    private val context = GenericXmlApplicationContext(Main::class.java)

    //@Autowiring("java.lang.String") /* <- throws 'Class specified in annotation is not compatible' exception*/
    @Autowiring("com.ukma.kutsyk.domain.LowerCasingInterceptor")
    private var activeInterceptor: Interceptor? = null

    private class ObjectInfo internal constructor(o: Any, name: String?) {

        private var name = "Anonymous"
        private var className: String
        private var superClassName: String
        private var implementedInterfaces: Array<Class<*>>
        private var fields: Array<Field>
        private var constructors: Array<Constructor<*>>
        private var methods: Array<Method>

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

                //In order to get formal method parameters names via reflection, they must be present in .class file.
                //By default Java compiler discards this information,
                //so you'll get arg0, arg1... instead of real informative names.
                //In order to have the method parameters names included in the .class files,
                //you must compile .java files with -parameters compile option.
                //To do this in Eclipse, go to Window > Preferences > Java > Compiler and
                //check the box 'Store information about method parameters (usable via reflection)'
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
        context.load(Main::class.java!!.getResource("/ExampleConfiguration.xml").getPath())
        context.setValidating(true)
        context.setParserType(ParserTypes.SAX)

        val factory = context.getBeanFactory()

        val greetingService = factory?.bean("greetingService", GreetingService::class.java) as GreetingService
        System.out.println(greetingService.getMessage())

        val bus = factory?.bean("bus", Transport::class.java) as Bus
        bus.transport()

        val bus2 = factory?.bean("bus2", Transport::class.java) as Bus
        bus2.transport()

        val car = factory?.bean("car", Transport::class.java) as Car
        System.out.println(car.toString())

        //================
        //================ REFLECTION API DEMO
        //================
        println()
        println("================ REFLECTION API DEMO ================")
        println()

        println(ObjectInfo(bus, "bus").toString())
        println("==========")
        println(ObjectInfo(bus.toString(),
                "String representation of the 'bus' object").toString())

        //================
        //================ CUSTOM INTERCEPTOR AUTOWIRING THROUGH ANNOTATION DEMO
        //================
        //(to prevent recompiling by Eclipse, switch Project > Build Automatically off)
        val output = "TeST InTercepTor"
        println("Unintercepted string: " + output)
        System.out.println("Intercepted string: " + activeInterceptor!!.interceptOutputString(output))

        //This block is needed for being able to inspect currently loaded classes
        //with tools like Java VisualVM
        println("Press any key to exit...")
        try {
            System.`in`.read()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    Main().main(args)
}
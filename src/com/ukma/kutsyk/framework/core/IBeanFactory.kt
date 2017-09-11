package com.ukma.kutsyk.framework.core

import java.lang.reflect.InvocationTargetException

interface IBeanFactory {

    val classLibrary: HashMap<String, Pair<Class<out Any>?, Class<out Any>>>
            get() = hashMapOf(
                    Pair("boolean", Pair(Boolean::class.javaPrimitiveType, Boolean::class.java)),
                    Pair("byte", Pair(Byte::class.javaPrimitiveType, Byte::class.java)),
                    Pair("short", Pair(Short::class.javaPrimitiveType, Short::class.java)),
                    Pair("int", Pair(Int::class.javaPrimitiveType, Int::class.java)),
                    Pair("long", Pair(Long::class.javaPrimitiveType, Long::class.java)),
                    Pair("float", Pair(Float::class.javaPrimitiveType, Float::class.java)),
                    Pair("double", Pair(Double::class.javaPrimitiveType, Double::class.java)),
                    Pair("char", Pair(Char::class.javaPrimitiveType, Char::class.java)),
                    Pair("void", Pair(Void.TYPE, Void::class.java))
                    //From the Java language specification PoV, 'void' is not a primitive type.
                    //Still, void.class.isPrimitive() returns true,
                    //so it is included here to cover this option
            )

    fun bean(string: String): Any
    fun <T> bean(string: String, type: Class<T>): T
    fun interceptors(): Array<Any>

    fun primitiveClassForName(primitiveTypeName: String): Class<*>?
            = classLibrary.get(primitiveTypeName)?.first

    fun wrapperClassValueForPrimitiveType(primitiveType: Class<*>, stringValue: String): Any? {
        var res: Any? = null

        try {
            res = classLibrary.get(primitiveType.name)?.second
                    ?.getMethod("valueOf", String::class.java)
                    ?.invoke(null, stringValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }
}
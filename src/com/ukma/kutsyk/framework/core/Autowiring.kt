package com.ukma.kutsyk.framework.core

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Autowiring(val value: String = "")

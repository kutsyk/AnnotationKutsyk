package com.ukma.kutsyk.homework_1.injection.`interface`

import com.ukma.kutsyk.framework.core.GenericXmlApplicationContext
import com.ukma.kutsyk.framework.core.ParserTypes
import com.ukma.kutsyk.practice_1.Main
import com.ukma.kutsyk.practice_1.domain.transport.Transport

interface IDependentClass
{
    fun DoSomethingInDependentClass()
}

interface IInjectDependent
{
    fun InjectDependent(dependentClass: IDependentClass)
}

class MainClass: IInjectDependent
{
    private var dependentClass: IDependentClass? = null

    fun DoSomething() = dependentClass?.DoSomethingInDependentClass()

    override fun InjectDependent(dependentClass: IDependentClass) {
        this.dependentClass = dependentClass
    }
}

class DependentClass1: IDependentClass
{
    override fun DoSomethingInDependentClass() {
        println("Hello from DependentClass1: I can be injected into MainClass")
    }
}

class DependentClass2: IDependentClass
{
    override fun DoSomethingInDependentClass() {
        println("Hello from DependentClass2: I can be injected as well")
    }
}

fun main(args: Array<String>)
{
    val dep1 = DependentClass1()
    val dep2 = DependentClass2()
    val mainClass = MainClass()
    mainClass.InjectDependent(dep1)
    mainClass.DoSomething()
    mainClass.InjectDependent(dep2)
    mainClass.DoSomething()
}
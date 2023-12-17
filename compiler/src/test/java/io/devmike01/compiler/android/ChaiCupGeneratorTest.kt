package io.devmike01.compiler.android

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChaiCupGeneratorTest {

    companion object{
        const val FOOD_PAGE_NO_ARG  = "/io.devmike01.chaicup.FoodPage?arg1=Hello&arg2=World"
    }
    @Test
    fun text_extractArgs() {
        val arg = ChaiCupGenerator.getRoute(FOOD_PAGE_NO_ARG, mapOf("arg1" to "Hello", "arg2" to "World"))
        Assert.assertEquals(arg, "$FOOD_PAGE_NO_ARG?arg1=Hello&arg2=World")
    }

    //parseArguments
    @Test
    fun text_parseArguments() {
        val arg = ChaiCupGenerator.parseArguments(FOOD_PAGE_NO_ARG, "hello world", "How are you")
        Assert.assertEquals(arg, "$FOOD_PAGE_NO_ARG?arg1=hello world&arg2=How are you")
    }
}
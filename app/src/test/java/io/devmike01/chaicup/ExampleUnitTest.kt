package io.devmike01.chaicup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val upperCase = "(.*[A-Z].*)"
        val matchesUpper =
            Pattern.matches(upperCase, "A")
        //Pattern.compile(upperCase)
        assertTrue(matchesUpper)
    }
}
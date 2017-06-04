package ru.terrakok.gitlabclient.model.utils

import junit.framework.Assert
import org.junit.Test

/**
 * @author Artur Badretdinov (Gaket)
 * *         20.12.2016.
 */
class StringUtilsKtTest {

    @Test
    fun get_esixting_code_from_url() {
        val expectedState = "happiness"
        val expectedCode = "helloReader"

        val testUrl = "http://something.com/test?code=" + expectedCode + "&state=" + expectedState

        val state = getQueryParameterFromUri(testUrl, "state")
        val code = getQueryParameterFromUri(testUrl, "code")

        Assert.assertEquals(expectedState, state)
        Assert.assertEquals(expectedCode, code)
    }

    @Test
    fun get_unesixting_code_from_url() {
        val testUrl = "http://something.com/test?code=100500" // there is no state

        val state = getQueryParameterFromUri(testUrl, "state")

        Assert.assertEquals("", state)
    }
}
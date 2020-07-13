package gitfox.util

import kotlin.browser.window

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.05.17
 */
internal actual class Base64Tools actual constructor() {
    actual fun decode(input: String): String = window.atob(input)
    actual fun encode(input: String): String = window.btoa(input)
}
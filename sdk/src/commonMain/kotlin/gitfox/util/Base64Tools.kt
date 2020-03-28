package gitfox.util

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.05.17
 */
internal expect class Base64Tools() {
    fun decode(input: String): String
    fun encode(input: String): String
}

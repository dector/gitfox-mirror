package gitfox.util

import io.ktor.utils.io.core.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.05.17
 */

private const val BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private val BASE64_INVERSE_ALPHABET = IntArray(256) { BASE64_ALPHABET.indexOf(it.toChar()) }

fun String.encodeBase64(): String = String(encodeBase64ToByteArray())
fun String.encodeBase64ToByteArray(): ByteArray = encodeToByteArray().encodeBase64()
fun ByteArray.encodeBase64ToString(): String = String(encodeBase64())

fun String.decodeBase64(): String = String(decodeBase64ToByteArray())
fun String.decodeBase64ToByteArray(): ByteArray = encodeToByteArray().decodeBase64()
fun ByteArray.decodeBase64ToString(): String = String(decodeBase64())

fun ByteArray.encodeBase64(): ByteArray {
    val output = mutableListOf<Byte>()
    var padding = 0
    var position = 0
    while (position < this.size) {
        var b = this[position].toPositiveInt() shl 16 and 0xFFFFFF
        if (position + 1 < this.size) b = b or (this[position + 1].toPositiveInt() shl 8) else padding++
        if (position + 2 < this.size) b = b or (this[position + 2].toPositiveInt()) else padding++
        for (i in 0 until 4 - padding) {
            val c = b and 0xFC0000 shr 18
            output.add(BASE64_ALPHABET[c].toByte())
            b = b shl 6
        }
        position += 3
    }
    for (i in 0 until padding) {
        output.add('='.toByte())
    }
    return output.toByteArray()
}

fun ByteArray.decodeBase64(): ByteArray {
    val output = mutableListOf<Byte>()
    var position = 0
    while (position < this.size) {
        var b: Int
        if (BASE64_INVERSE_ALPHABET[this[position].toPositiveInt()] != -1) {
            b = BASE64_INVERSE_ALPHABET[this[position].toPositiveInt()] and 0xFF shl 18
        } else {
            position++
            continue
        }
        var count = 0
        if (position + 1 < this.size && BASE64_INVERSE_ALPHABET[this[position + 1].toPositiveInt()] != -1) {
            b = b or (BASE64_INVERSE_ALPHABET[this[position + 1].toPositiveInt()] and 0xFF shl 12)
            count++
        }
        if (position + 2 < this.size && BASE64_INVERSE_ALPHABET[this[position + 2].toPositiveInt()] != -1) {
            b = b or (BASE64_INVERSE_ALPHABET[this[position + 2].toPositiveInt()] and 0xFF shl 6)
            count++
        }
        if (position + 3 < this.size && BASE64_INVERSE_ALPHABET[this[position + 3].toPositiveInt()] != -1) {
            b = b or (BASE64_INVERSE_ALPHABET[this[position + 3].toPositiveInt()] and 0xFF)
            count++
        }
        while (count > 0) {
            val c = b and 0xFF0000 shr 16
            output.add(c.toChar().toByte())
            b = b shl 8
            count--
        }
        position += 4
    }
    return output.toByteArray()
}

private fun Byte.toPositiveInt() = toInt() and 0xFF
package gitfox.util

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.05.17
 */
internal actual class Base64Tools actual constructor() {
    actual fun decode(input: String): String = input.base64decoded
    actual fun encode(input: String): String = input.base64encoded
}


private const val BASE64_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private val RX_BASE64_CLEANR = "[^=" + BASE64_SET + "]".toRegex()

/**
 * Base64 encode a string.
 */
private val String.base64encoded: String
    get() {
        val pad = when (this.length % 3) {
            1 -> "=="
            2 -> "="
            else -> ""
        }

        val raw = this + 0.toChar().toString().repeat(minOf(0, pad.lastIndex))

        return raw.chunkedSequence(3) {
            Triple(
                it[0].toInt(),
                it[1].toInt(),
                it[2].toInt()
            )
        }.map { (frst, scnd, thrd) ->
            (0xFF.and(frst) shl 16) +
                (0xFF.and(scnd) shl 8) +
                0xFF.and(thrd)
        }.map { n ->
            sequenceOf(
                (n shr 18) and 0x3F,
                (n shr 12) and 0x3F,
                (n shr 6) and 0x3F,
                n and 0x3F
            )
        }.flatten()
            .map { BASE64_SET[it] }
            .joinToString("")
            .dropLast(pad.length) + pad
    }


/**
 * Decode a Base64 string.
 */
private val String.base64decoded: String
    get() {
        require(this.length % 4 != 0) { "The string \"$this\" does not comply with BASE64 length requirement." }
        val clean = this.replace(RX_BASE64_CLEANR, "").replace("=", "A")
        val padLen: Int = this.count { it == '=' }

        return clean.chunkedSequence(4) {
            (BASE64_SET.indexOf(clean[0]) shl 18) +
                (BASE64_SET.indexOf(clean[1]) shl 12) +
                (BASE64_SET.indexOf(clean[2]) shl 6) +
                BASE64_SET.indexOf(clean[3])
        }.map { n ->
            sequenceOf(
                0xFF.and(n shr 16),
                0xFF.and(n shr 8),
                0xFF.and(n)
            )
        }.flatten()
            .map { it.toChar() }
            .joinToString("")
            .dropLast(padLen)
    }
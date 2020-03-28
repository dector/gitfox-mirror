package gitfox.util

import android.util.Base64

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.05.17
 */
internal actual class Base64Tools actual constructor() {
    actual fun decode(input: String) = String(Base64.decode(input.toByteArray(), Base64.DEFAULT))
    actual fun encode(input: String) = String(Base64.encode(input.toByteArray(), Base64.DEFAULT))
}
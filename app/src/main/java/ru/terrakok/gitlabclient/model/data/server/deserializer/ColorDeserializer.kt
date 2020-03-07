package ru.terrakok.gitlabclient.model.data.server.deserializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import ru.terrakok.gitlabclient.entity.Color

@Serializer(forClass = Color::class)
object ColorDeserializer: KSerializer<Color> {
    override fun deserialize(decoder: Decoder): Color {
        // The color of the label given in 6-digit hex notation with leading ‘#’ sign (e.g. #FFAABB)
        // or one of the CSS color names. So according to CSS color names it can be named differently on Android.
        val colorString = decoder.decodeString()
        val colorInt = try {
            android.graphics.Color.parseColor(colorString)
        } catch (e: IllegalArgumentException) {
            android.graphics.Color.GREEN
        }
        return Color(colorString, colorInt)
    }
}

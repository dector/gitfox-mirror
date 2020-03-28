package gitfox.model.data.server.deserializer

import gitfox.entity.Color
import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer

@Serializer(forClass = Color::class)
internal object ColorDeserializer : KSerializer<Color> {
    override fun deserialize(decoder: Decoder): Color {
        // The color of the label given in 6-digit hex notation with leading ‘#’ sign (e.g. #FFAABB)
        // or one of the CSS color names. So according to CSS color names it can be named differently on Android.
        return Color(decoder.decodeString())
    }
}

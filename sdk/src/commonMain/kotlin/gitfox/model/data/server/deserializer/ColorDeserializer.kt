package gitfox.model.data.server.deserializer

import gitfox.entity.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = Color::class)
internal object ColorDeserializer : KSerializer<Color> {
    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Color =
        Color(decoder.decodeString())
}

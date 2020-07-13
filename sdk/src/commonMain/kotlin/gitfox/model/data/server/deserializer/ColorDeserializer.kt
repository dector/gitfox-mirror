package gitfox.model.data.server.deserializer

import gitfox.entity.Color
import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer

@Serializer(forClass = Color::class)
internal object ColorDeserializer : KSerializer<Color> {
    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.name)
    }

    override fun deserialize(decoder: Decoder): Color =
        Color(decoder.decodeString())
}

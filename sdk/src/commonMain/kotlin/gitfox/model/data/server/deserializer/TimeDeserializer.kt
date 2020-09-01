package gitfox.model.data.server.deserializer

import gitfox.entity.Time
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = Time::class)
internal object TimeDeserializer : KSerializer<Time> {

    override fun serialize(encoder: Encoder, value: Time) {
        encoder.encodeString(value.isoString)
    }

    override fun deserialize(decoder: Decoder): Time =
        Time(decoder.decodeString())
}

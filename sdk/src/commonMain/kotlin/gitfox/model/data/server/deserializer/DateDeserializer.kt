package gitfox.model.data.server.deserializer

import gitfox.entity.Date
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = Date::class)
internal object DateDeserializer : KSerializer<Date> {

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.isoString)
    }

    override fun deserialize(decoder: Decoder): Date =
        Date(decoder.decodeString())
}

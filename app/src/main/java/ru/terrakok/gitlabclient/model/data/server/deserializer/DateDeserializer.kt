package ru.terrakok.gitlabclient.model.data.server.deserializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import ru.terrakok.gitlabclient.entity.Date

@Serializer(forClass = Date::class)
object DateDeserializer : KSerializer<Date> {

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(value.isoString)
    }

    override fun deserialize(decoder: Decoder): Date =
        Date(decoder.decodeString())
}

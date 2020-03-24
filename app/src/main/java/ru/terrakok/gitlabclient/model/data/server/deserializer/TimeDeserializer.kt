package ru.terrakok.gitlabclient.model.data.server.deserializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import ru.terrakok.gitlabclient.entity.Time

@Serializer(forClass = Time::class)
object TimeDeserializer : KSerializer<Time> {

    override fun serialize(encoder: Encoder, value: Time) {
        encoder.encodeString(value.isoString)
    }

    override fun deserialize(decoder: Decoder): Time =
        Time(decoder.decodeString())
}

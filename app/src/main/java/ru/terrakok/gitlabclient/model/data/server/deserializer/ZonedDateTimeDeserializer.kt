package ru.terrakok.gitlabclient.model.data.server.deserializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import org.threeten.bp.ZonedDateTime

@Serializer(forClass = ZonedDateTime::class)
object ZonedDateTimeDeserializer: KSerializer<ZonedDateTime> {
    override fun deserialize(decoder: Decoder): ZonedDateTime =
        ZonedDateTime.parse(decoder.decodeString())
}

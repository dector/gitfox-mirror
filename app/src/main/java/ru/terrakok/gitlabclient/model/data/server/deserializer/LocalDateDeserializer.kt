package ru.terrakok.gitlabclient.model.data.server.deserializer

import kotlinx.serialization.Decoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Serializer(forClass = LocalDate::class)
object LocalDateDeserializer: KSerializer<LocalDate> {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun deserialize(decoder: Decoder): LocalDate =
        LocalDate.parse(decoder.decodeString(), dateTimeFormatter)
}

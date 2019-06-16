package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 16.05.19.
 */
class LocalDateDeserializer : JsonDeserializer<LocalDate> {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun deserialize(
        json: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): LocalDate = LocalDate.parse(json.asJsonPrimitive.asString, dateTimeFormatter)
}
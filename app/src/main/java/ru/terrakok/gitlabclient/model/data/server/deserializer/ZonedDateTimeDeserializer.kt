package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.ZonedDateTime
import java.lang.reflect.Type

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ZonedDateTime = ZonedDateTime.parse(json.asJsonPrimitive.asString)
}
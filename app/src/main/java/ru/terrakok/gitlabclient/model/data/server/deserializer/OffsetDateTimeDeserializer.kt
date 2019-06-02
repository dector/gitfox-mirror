package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.OffsetDateTime
import java.lang.reflect.Type

class OffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): OffsetDateTime = OffsetDateTime.parse(json.asJsonPrimitive.asString)
}
package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import org.threeten.bp.ZonedDateTime

class ZonedDateTimeDeserializer : JsonDeserializer<ZonedDateTime> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext?
    ): ZonedDateTime = ZonedDateTime.parse(json.asJsonPrimitive.asString)
}
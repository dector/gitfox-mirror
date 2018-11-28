package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import java.lang.reflect.Type
import java.util.*

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 27.02.18.
 */
class DateDeserializer : JsonDeserializer<LocalDateTime> {
    private val offset = TimeZone.getDefault().rawOffset / 1000L

    override fun deserialize(
        json: JsonElement,
        type: Type,
        jsonDeserializationContext: JsonDeserializationContext
    ): LocalDateTime {
        return ZonedDateTime.parse(json.asJsonPrimitive.asString).plusSeconds(offset).toLocalDateTime()
    }
}
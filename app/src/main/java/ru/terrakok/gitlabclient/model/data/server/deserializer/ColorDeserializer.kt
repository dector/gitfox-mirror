package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.terrakok.gitlabclient.entity.Color
import java.lang.reflect.Type

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 04.01.19.
 */
class ColorDeserializer : JsonDeserializer<Color> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Color {
        val colorString = json.asJsonPrimitive.asString
        val colorInt = try {
            android.graphics.Color.parseColor(colorString)
        } catch (e: IllegalArgumentException) {
            android.graphics.Color.GREEN
        }
        return Color(colorString, colorInt)
    }
}
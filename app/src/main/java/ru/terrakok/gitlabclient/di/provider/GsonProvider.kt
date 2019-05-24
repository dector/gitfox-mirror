package ru.terrakok.gitlabclient.di.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import ru.terrakok.gitlabclient.entity.Color
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.model.data.server.deserializer.ColorDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.LocalDateTimeDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.LocalDateDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.TodoDeserializer
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 14.09.17
 */
class GsonProvider @Inject constructor() : Provider<Gson> {

    override fun get(): Gson =
        GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .registerTypeAdapter(Todo::class.java, TodoDeserializer())
            .registerTypeAdapter(Color::class.java, ColorDeserializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()
}
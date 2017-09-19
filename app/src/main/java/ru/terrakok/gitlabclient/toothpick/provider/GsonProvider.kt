package ru.terrakok.gitlabclient.toothpick.provider

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoDeserializer
import javax.inject.Provider

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 14.09.17
 */
class GsonProvider : Provider<Gson> {

    override fun get(): Gson =
            GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .registerTypeAdapter(Todo::class.java, TodoDeserializer())
                    .create()
}
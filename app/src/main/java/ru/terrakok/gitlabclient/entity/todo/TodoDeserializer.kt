package ru.terrakok.gitlabclient.entity.todo

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import ru.terrakok.gitlabclient.entity.Author
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.target.Target
import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.target.issue.Issue
import ru.terrakok.gitlabclient.entity.target.mergerequest.MergeRequest
import java.lang.reflect.Type
import java.util.*

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 13.09.17
 */
class TodoDeserializer : JsonDeserializer<Todo> {

    override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
    ) = if (json != null && context != null) {
        val jsonObject = json.asJsonObject
        val targetType = context.deserialize<TargetType>(
                jsonObject.get("target_type"),
                TargetType::class.java
        )
        Todo(
                jsonObject.get("id").asLong,
                context.deserialize<Project>(jsonObject.get("project"), Project::class.java),
                context.deserialize<Author>(jsonObject.get("author"), Author::class.java),
                context.deserialize<TodoAction>(
                        jsonObject.get("action_name"),
                        TodoAction::class.java
                ),
                targetType,
                context.deserialize<Target>(
                        jsonObject.get("target"),
                        when (targetType) {
                            TargetType.ISSUE -> Issue::class.java
                            TargetType.MERGE_REQUEST -> MergeRequest::class.java
                            else -> throw JsonParseException("See target_type in GitLab Todo API.")
                        }
                ),
                jsonObject.get("target_url").asString,
                jsonObject.get("body").asString,
                context.deserialize<TodoState>(jsonObject.get("state"), TodoState::class.java),
                context.deserialize<Date>(jsonObject.get("created_at"), Date::class.java)
        )
    } else {
        throw JsonParseException("Configure Gson in GsonProvider.")
    }
}
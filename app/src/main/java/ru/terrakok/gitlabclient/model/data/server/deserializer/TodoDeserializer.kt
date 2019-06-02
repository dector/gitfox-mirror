package ru.terrakok.gitlabclient.model.data.server.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import org.threeten.bp.OffsetDateTime
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.target.Target
import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.target.issue.Issue
import ru.terrakok.gitlabclient.entity.target.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.entity.todo.TodoState
import java.lang.reflect.Type

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
            context.deserialize<ShortUser>(jsonObject.get("author"), ShortUser::class.java),
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
            context.deserialize<OffsetDateTime>(jsonObject.get("created_at"), OffsetDateTime::class.java)
        )
    } else {
        throw JsonParseException("Configure Gson in GsonProvider.")
    }
}
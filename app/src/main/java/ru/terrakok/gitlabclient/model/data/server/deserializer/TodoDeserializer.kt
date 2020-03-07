package ru.terrakok.gitlabclient.model.data.server.deserializer

import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.Target
import ru.terrakok.gitlabclient.entity.TargetType

object TodoDeserializer : KSerializer<Todo> {

    override val descriptor = SerialDescriptor("Todo") {
        element("id", Long.serializer().descriptor)
        element("project", Project.serializer().descriptor)
        element("author", ShortUser.serializer().descriptor)
        element("action_name", TodoAction.serializer().descriptor)
        element("target_type", TargetType.serializer().descriptor)
        element("target", PolymorphicSerializer(Target::class).descriptor)
        element("target_url", String.serializer().descriptor)
        element("body", String.serializer().descriptor)
        element("state", TodoState.serializer().descriptor)
        element("created_at", ZonedDateTimeDeserializer.descriptor)
    }

    override fun serialize(encoder: Encoder, value: Todo) {
        val compositeOutput = encoder.beginStructure(descriptor)
        compositeOutput.encodeLongElement(descriptor, 0, value.id)
        compositeOutput.encodeSerializableElement(descriptor, 1, Project.serializer(), value.project)
        compositeOutput.encodeSerializableElement(descriptor, 2, ShortUser.serializer(), value.author)
        compositeOutput.encodeSerializableElement(descriptor, 3, TodoAction.serializer(), value.actionName)
        compositeOutput.encodeSerializableElement(descriptor, 4, TargetType.serializer(), value.targetType)
        when (value.target) {
            is Target.Issue -> compositeOutput.encodeSerializableElement(descriptor, 5, Target.Issue.serializer(), value.target)
            is Target.MergeRequest -> compositeOutput.encodeSerializableElement(descriptor, 5, Target.MergeRequest.serializer(), value.target)
        }
        compositeOutput.encodeStringElement(descriptor, 6, value.targetUrl)
        compositeOutput.encodeStringElement(descriptor, 7, value.body)
        compositeOutput.encodeSerializableElement(descriptor, 8, TodoState.serializer(), value.state)
        compositeOutput.encodeSerializableElement(descriptor, 9, ZonedDateTimeDeserializer, value.createdAt)
        compositeOutput.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Todo {

        var id: Long? = null
        var project: Project? = null
        var author: ShortUser? = null
        var actionName: TodoAction? = null
        var targetType: TargetType? = null
        var target: Target? = null
        var targetUrl: String? = null
        var body: String? = null
        var state: TodoState? = null
        var createdAt: ZonedDateTime? = null

        val dec: CompositeDecoder = decoder.beginStructure(descriptor)
        loop@ while (true) {
            when (val i = dec.decodeElementIndex(descriptor)) {
                CompositeDecoder.READ_DONE -> break@loop
                0 -> id = dec.decodeLongElement(descriptor, i)
                1 -> project = dec.decodeSerializableElement(descriptor, i, Project.serializer())
                2 -> author = dec.decodeSerializableElement(descriptor, i, ShortUser.serializer())
                3 -> actionName = dec.decodeSerializableElement(descriptor, i, TodoAction.serializer())
                4 -> targetType = dec.decodeSerializableElement(descriptor, i, TargetType.serializer())
                5 -> target = when(targetType) {
                    TargetType.ISSUE -> dec.decodeSerializableElement(descriptor, i, Target.Issue.serializer())
                    TargetType.MERGE_REQUEST -> dec.decodeSerializableElement(descriptor, i, Target.MergeRequest.serializer())
                    else -> throw SerializationException("Unknown targetType $targetType")
                }
                6 -> targetUrl = dec.decodeStringElement(descriptor, i)
                7 -> body = dec.decodeStringElement(descriptor, i)
                8 -> state = dec.decodeSerializableElement(descriptor, i, TodoState.serializer())
                9 -> createdAt = dec.decodeSerializableElement(descriptor, i, ZonedDateTimeDeserializer)
                else -> throw SerializationException("Unknown index $i")
            }
        }
        dec.endStructure(descriptor)

        return Todo(
            id ?: throw MissingFieldException("id"),
            project ?: throw MissingFieldException("project"),
            author ?: throw MissingFieldException("author"),
            actionName ?: throw MissingFieldException("action_name"),
            targetType ?: throw MissingFieldException("target_type"),
            target ?: throw MissingFieldException("target"),
            targetUrl ?: throw MissingFieldException("target_url"),
            body ?: throw MissingFieldException("body"),
            state ?: throw MissingFieldException("state"),
            createdAt ?: throw MissingFieldException("created_at")
        )
    }
}

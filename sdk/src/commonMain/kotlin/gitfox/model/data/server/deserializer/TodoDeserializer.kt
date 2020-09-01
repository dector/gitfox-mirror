package gitfox.model.data.server.deserializer

import gitfox.entity.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.*

internal object TodoDeserializer : KSerializer<Todo> {

    override val descriptor = buildClassSerialDescriptor("Todo") {
        element("id", Long.serializer().descriptor)
        element("project", Project.serializer().descriptor)
        element("author", ShortUser.serializer().descriptor)
        element("action_name", TodoAction.serializer().descriptor)
        element("target_type", TargetType.serializer().descriptor)
        element("target", PolymorphicSerializer(Target::class).descriptor)
        element("target_url", String.serializer().descriptor)
        element("body", String.serializer().descriptor)
        element("state", TodoState.serializer().descriptor)
        element("created_at", TimeDeserializer.descriptor)
    }

    override fun serialize(encoder: Encoder, value: Todo) {
        encoder.encodeStructure(descriptor) {
            encodeLongElement(descriptor, 0, value.id)
            encodeSerializableElement(descriptor, 1, Project.serializer(), value.project)
            encodeSerializableElement(descriptor, 2, ShortUser.serializer(), value.author)
            encodeSerializableElement(descriptor, 3, TodoAction.serializer(), value.actionName)
            encodeSerializableElement(descriptor, 4, TargetType.serializer(), value.targetType)
            when (value.target) {
                is Target.Issue -> encodeSerializableElement(descriptor, 5, Target.Issue.serializer(), value.target)
                is Target.MergeRequest -> encodeSerializableElement(descriptor, 5, Target.MergeRequest.serializer(), value.target)
            }
            encodeStringElement(descriptor, 6, value.targetUrl)
            encodeStringElement(descriptor, 7, value.body)
            encodeSerializableElement(descriptor, 8, TodoState.serializer(), value.state)
            encodeSerializableElement(descriptor, 9, TimeDeserializer, value.createdAt)
        }
    }

    override fun deserialize(decoder: Decoder): Todo = decoder.decodeStructure(descriptor) {
        var id: Long? = null
        var project: Project? = null
        var author: ShortUser? = null
        var actionName: TodoAction? = null
        var targetType: TargetType? = null
        var target: Target? = null
        var targetUrl: String? = null
        var body: String? = null
        var state: TodoState? = null
        var createdAt: Time? = null
        while (true) {
            when (val i = decodeElementIndex(descriptor)) {
                0 -> id = decodeLongElement(descriptor, i)
                1 -> project = decodeSerializableElement(descriptor, i, Project.serializer())
                2 -> author = decodeSerializableElement(descriptor, i, ShortUser.serializer())
                3 -> actionName = decodeSerializableElement(descriptor, i, TodoAction.serializer())
                4 -> targetType = decodeSerializableElement(descriptor, i, TargetType.serializer())
                5 -> target = when (targetType) {
                    TargetType.ISSUE -> decodeSerializableElement(descriptor, i, Target.Issue.serializer())
                    TargetType.MERGE_REQUEST -> decodeSerializableElement(descriptor, i, Target.MergeRequest.serializer())
                    else -> throw SerializationException("Unknown targetType $targetType")
                }
                6 -> targetUrl = decodeStringElement(descriptor, i)
                7 -> body = decodeStringElement(descriptor, i)
                8 -> state = decodeSerializableElement(descriptor, i, TodoState.serializer())
                9 -> createdAt = decodeSerializableElement(descriptor, i, TimeDeserializer)
                CompositeDecoder.DECODE_DONE -> break
                else -> throw SerializationException("Unknown index $i")
            }
        }
        Todo(id!!, project!!, author!!, actionName!!, targetType!!, target!!, targetUrl!!, body!!, state!!, createdAt!!)
    }
}

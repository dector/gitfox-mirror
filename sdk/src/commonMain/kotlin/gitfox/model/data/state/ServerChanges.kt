package gitfox.model.data.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch

internal class ServerChanges : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    private val issueChannel = BroadcastChannel<Long>(1)
    private val mergeRequestChannel = BroadcastChannel<Long>(1)
    private val projectChannel = BroadcastChannel<Long>(1)
    private val labelChannel = BroadcastChannel<Long>(1)
    private val milestoneChannel = BroadcastChannel<Long>(1)
    private val todoChannel = BroadcastChannel<Long>(1)
    private val userChannel = BroadcastChannel<Long>(1)
    private val memberChannel = BroadcastChannel<Long>(1)

    val issueChanges = issueChannel.asFlow()
    val mergeRequestChanges = mergeRequestChannel.asFlow()
    val projectChanges = projectChannel.asFlow()
    val labelChanges = labelChannel.asFlow()
    val milestoneChanges = milestoneChannel.asFlow()
    val todoChanges = todoChannel.asFlow()
    val userChanges = userChannel.asFlow()
    val memberChanges = memberChannel.asFlow()

    fun issueChanged(id: Long = -1) {
        launch { issueChannel.send(id) }
    }

    fun mergeRequestChanged(id: Long = -1) {
        launch { mergeRequestChannel.send(id) }
    }

    fun projectChanged(id: Long = -1) {
        launch { projectChannel.send(id) }
    }

    fun labelChanged(id: Long = -1) {
        launch { labelChannel.send(id) }
    }

    fun milestoneChanged(id: Long = -1) {
        launch { milestoneChannel.send(id) }
    }

    fun todoChanged(id: Long = -1) {
        launch { todoChannel.send(id) }
    }

    fun userChanged(id: Long = -1) {
        launch { userChannel.send(id) }
    }

    fun memberChanged(id: Long = -1) {
        launch { memberChannel.send(id) }
    }
}

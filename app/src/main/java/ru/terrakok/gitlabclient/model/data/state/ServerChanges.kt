package ru.terrakok.gitlabclient.model.data.state

import com.jakewharton.rxrelay2.PublishRelay
import javax.inject.Inject
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

class ServerChanges @Inject constructor(
    schedulers: SchedulersProvider
) {
    private val issueRelay = PublishRelay.create<Long>()
    private val mergeRequestRelay = PublishRelay.create<Long>()
    private val projectRelay = PublishRelay.create<Long>()
    private val labelRelay = PublishRelay.create<Long>()
    private val milestoneRelay = PublishRelay.create<Long>()
    private val todoRelay = PublishRelay.create<Long>()
    private val userRelay = PublishRelay.create<Long>()
    private val memberRelay = PublishRelay.create<Long>()

    val issueChanges = issueRelay.hide().observeOn(schedulers.ui())
    val mergeRequestChanges = mergeRequestRelay.hide().observeOn(schedulers.ui())
    val projectChanges = projectRelay.hide().observeOn(schedulers.ui())
    val labelChanges = labelRelay.hide().observeOn(schedulers.ui())
    val milestoneChanges = milestoneRelay.hide().observeOn(schedulers.ui())
    val todoChanges = todoRelay.hide().observeOn(schedulers.ui())
    val userChanges = userRelay.hide().observeOn(schedulers.ui())
    val memberChanges = memberRelay.hide().observeOn(schedulers.ui())

    fun issueChanged(id: Long = -1) {
        issueRelay.accept(id)
    }

    fun mergeRequestChanged(id: Long = -1) {
        mergeRequestRelay.accept(id)
    }

    fun projectChanged(id: Long = -1) {
        projectRelay.accept(id)
    }

    fun labelChanged(id: Long = -1) {
        labelRelay.accept(id)
    }

    fun milestoneChanged(id: Long = -1) {
        milestoneRelay.accept(id)
    }

    fun todoChanged(id: Long = -1) {
        todoRelay.accept(id)
    }

    fun userChanged(id: Long = -1) {
        userRelay.accept(id)
    }

    fun memberChanged(id: Long = -1) {
        memberRelay.accept(id)
    }
}

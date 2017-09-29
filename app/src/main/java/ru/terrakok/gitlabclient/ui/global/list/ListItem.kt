package ru.terrakok.gitlabclient.ui.global.list

import ru.terrakok.gitlabclient.entity.Issue
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
sealed class ListItem {
    class ProgressItem : ListItem()
    class ProjectItem(val project: Project) : ListItem()
    class IssueItem(val issue: Issue) : ListItem()
    class MergeRequestItem(val mergeRequest: MergeRequest) : ListItem()
    class EventItem(val event: Event) : ListItem()
}
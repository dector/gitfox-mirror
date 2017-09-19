package ru.terrakok.gitlabclient.ui.global.list

import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.target.issue.Issue

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
sealed class ListItem {
    class ProgressItem : ListItem()
    class ProjectItem(val project: Project) : ListItem()
    class IssueItem(val issue: Issue) : ListItem()
    class EventItem(val event: Event) : ListItem()
}
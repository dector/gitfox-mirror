package ru.terrakok.gitlabclient.ui.global.list

import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.todo.Todo

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
sealed class ListItem {
    class ProgressItem : ListItem()
    class ProjectItem(val project: Project) : ListItem()
    class AppLibraryItem(val item: AppLibrary) : ListItem()
    class TargetHeaderItem(val item: TargetHeader) : ListItem()
    class TodoItem(val todo: Todo) : ListItem()
}
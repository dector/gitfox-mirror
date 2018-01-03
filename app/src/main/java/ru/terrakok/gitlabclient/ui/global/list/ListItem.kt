package ru.terrakok.gitlabclient.ui.global.list

import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
sealed class ListItem {
    class ProgressItem : ListItem()
    data class ProjectItem(val project: Project) : ListItem()
    data class AppLibraryItem(val item: AppLibrary) : ListItem()
    data class TargetHeaderItem(val item: TargetHeader) : ListItem()
}
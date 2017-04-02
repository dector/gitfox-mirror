package ru.terrakok.gitlabclient.ui.main

import ru.terrakok.gitlabclient.entity.Project

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
sealed class ProjectsListItem {
    class ProjectItem(val projest: Project) : ProjectsListItem()
    class ProgressItem : ProjectsListItem()
}
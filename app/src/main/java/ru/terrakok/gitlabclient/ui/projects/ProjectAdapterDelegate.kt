package ru.terrakok.gitlabclient.ui.projects

import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_project.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class ProjectAdapterDelegate : AbsListItemAdapterDelegate<ProjectsListItem.ProjectItem, ProjectsListItem, ProjectAdapterDelegate.ProjectViewHolder>() {
    override fun isForViewType(item: ProjectsListItem, items: MutableList<ProjectsListItem>, position: Int)
            = item is ProjectsListItem.ProjectItem

    override fun onCreateViewHolder(parent: ViewGroup)
            = ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false))

    override fun onBindViewHolder(item: ProjectsListItem.ProjectItem, viewHolder: ProjectViewHolder, payloads: MutableList<Any>)
            = viewHolder.bind(item.projest)

    class ProjectViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(project: Project) {
            view.titleTV.text = project.nameWithNamespace

            view.descriptionTV.visibility = if (project.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            view.descriptionTV.text = project.description

            project.name?.let {
                view.letterTV.text = it.first().toUpperCase().toString()
            }

            val colorRes = when (project.id % 5) {
                0L -> R.color.badge_color_1
                1L -> R.color.badge_color_2
                2L -> R.color.badge_color_3
                3L -> R.color.badge_color_4
                4L -> R.color.badge_color_5
                else -> R.color.badge_color_1
            }

            view.letterTV.background.setColorFilter(view.resources.getColor(colorRes), PorterDuff.Mode.SRC_IN)
        }
    }
}
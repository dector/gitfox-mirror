package ru.terrakok.gitlabclient.ui.projects

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_project.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.extension.color

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

            view.starsTV.text = project.starCount.toString()
            view.publicIV.setImageResource(when (project.visibility) {
                Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
                Visibility.INTERNAL -> R.drawable.ic_lock_outline_white_18dp
                else -> R.drawable.ic_globe_18dp
            })

            project.name?.let {
                view.letterTV.text = it.first().toUpperCase().toString()
            }

            val colorRes = when (project.id % 5) {
                0L -> R.color.accentRed
                1L -> R.color.accentOrange
                2L -> R.color.accentYellow
                3L -> R.color.accentMint
                4L -> R.color.accentGreen
                else -> R.color.accentRed
            }

            val letterTV = view.letterTV
            letterTV.background.setColorFilter(view.resources.color(colorRes), PorterDuff.Mode.SRC_IN)

            view.avatarIV.visibility = View.INVISIBLE
            Glide.with(view.context)
                    .load(project.avatarUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(object : BitmapImageViewTarget(view.avatarIV) {
                        override fun setResource(resource: Bitmap?) {
                            resource?.let {
                                view.avatarIV.visibility = View.VISIBLE
                                letterTV.background.setColorFilter(view.resources.color(R.color.white), PorterDuff.Mode.SRC_IN)
                                RoundedBitmapDrawableFactory.create(view.resources, it).run {
                                    this.isCircular = true
                                    view.avatarIV.setImageDrawable(this)
                                }
                            }
                        }
                    })
        }
    }
}
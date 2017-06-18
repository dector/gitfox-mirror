package ru.terrakok.gitlabclient.ui.projects

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_project.view.*
import kotlinx.android.synthetic.main.layout_avatar.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.common.Project
import ru.terrakok.gitlabclient.entity.common.Visibility
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.ui.global.list.ListItem

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class ProjectAdapterDelegate(private val clickListener: (Project) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.ProgressItem

    override fun onCreateViewHolder(parent: ViewGroup) =
            ProjectViewHolder(parent.inflate(R.layout.item_project), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as ProjectViewHolder).bind((items[position] as ListItem.ProjectItem).project)

    class ProjectViewHolder(val view: View, clickListener: (Project) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var project: Project

        init {
            view.setOnClickListener { clickListener.invoke(project) }
        }

        fun bind(project: Project) {
            this.project = project
            view.titleTV.text = project.nameWithNamespace

            view.descriptionTV.visibility = if (project.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            view.descriptionTV.text = project.description

            view.starsTV.text = project.starCount.toString()
            view.publicIV.setImageResource(when (project.visibility) {
                Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
                Visibility.INTERNAL -> R.drawable.ic_security_white_24dp
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
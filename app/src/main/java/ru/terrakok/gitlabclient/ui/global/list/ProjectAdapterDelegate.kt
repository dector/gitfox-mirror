package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_project.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.extension.loadRoundedImage

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class ProjectAdapterDelegate(private val clickListener: (Project) -> Unit) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
            items[position] is Project

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ProjectViewHolder(parent.inflate(R.layout.item_project), clickListener)

    override fun onBindViewHolder(items: MutableList<Any>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as ProjectViewHolder).bind(items[position] as Project)

    private class ProjectViewHolder(val view: View, clickListener: (Project) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var project: Project

        init {
            view.setOnClickListener { clickListener.invoke(project) }
        }

        fun bind(project: Project) {
            this.project = project
            view.titleTextView.text = project.nameWithNamespace

            view.descriptionTextView.visibility = if (project.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            view.descriptionTextView.text = project.description

            view.starsTextView.text = project.starCount.toString()
            view.forksTextView.text = project.forksCount.toString()

            view.iconImageView.setImageResource(when (project.visibility) {
                Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
                Visibility.INTERNAL -> R.drawable.ic_security_white_24dp
                else -> R.drawable.ic_globe_18dp
            })
            view.avatarImageView.loadRoundedImage(project.avatarUrl)
        }
    }
}
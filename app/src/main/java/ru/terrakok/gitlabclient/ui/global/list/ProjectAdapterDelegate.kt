package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_project.*
import kotlinx.android.synthetic.main.item_project.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.ui.global.view.custom.bindProject
import ru.terrakok.gitlabclient.util.getTintDrawable
import ru.terrakok.gitlabclient.util.inflate
import ru.terrakok.gitlabclient.util.setStartDrawable

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */

fun Project.isSame(other: Project) = id == other.id

class ProjectAdapterDelegate(private val clickListener: (Project) -> Unit) :
    AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is Project

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val root = parent.inflate(R.layout.item_project)
        with(root) {
            starsTextView.setStartDrawable(
                context.getTintDrawable(
                    R.drawable.ic_star_black_24dp,
                    R.color.colorPrimary
                )
            )
            forksTextView.setStartDrawable(
                context.getTintDrawable(
                    R.drawable.ic_fork,
                    R.color.colorPrimary
                )
            )
        }
        return ViewHolder(root)
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) =
        (viewHolder as ViewHolder).bind(items[position] as Project)

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var project: Project

        init {
            containerView.setOnClickListener { clickListener(project) }
        }

        fun bind(project: Project) {
            this.project = project
            titleTextView.text = project.nameWithNamespace

            descriptionTextView.visibility =
                if (project.description.isNullOrEmpty()) View.GONE else View.VISIBLE
            descriptionTextView.text = project.description

            starsTextView.text = project.starCount.toString()
            forksTextView.text = project.forksCount.toString()

            iconImageView.setImageResource(
                when (project.visibility) {
                    Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
                    Visibility.INTERNAL -> R.drawable.ic_security_white_24dp
                    else -> R.drawable.ic_globe_18dp
                }
            )
            avatarImageView.bindProject(project)
        }
    }
}

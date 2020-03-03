package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_project_file.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.util.inflate

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */

fun ProjectFile.isSame(other: ProjectFile) = id == other.id

class ProjectFileAdapterDelegate(
    private val fileClickListener: (ProjectFile) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is ProjectFile

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_project_file))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as ProjectFile)

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var item: ProjectFile

        init {
            containerView.setOnClickListener { fileClickListener(item) }
        }

        fun bind(item: ProjectFile) {
            this.item = item
            projectFileIcon.setImageResource(
                when (item.nodeType) {
                    RepositoryTreeNodeType.BLOB -> R.drawable.ic_file
                    RepositoryTreeNodeType.TREE -> R.drawable.ic_folder
                }
            )
            projectFileName.text = item.name
        }
    }
}

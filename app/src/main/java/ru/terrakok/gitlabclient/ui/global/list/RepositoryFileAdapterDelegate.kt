package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_repository_file.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType
import ru.terrakok.gitlabclient.entity.app.RepositoryFile
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
class RepositoryFileAdapterDelegate(
    private val fileClickListener: (RepositoryFile) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is RepositoryFile

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_repository_file))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as RepositoryFile)

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: RepositoryFile

        init {
            view.setOnClickListener { fileClickListener(item) }
        }

        fun bind(item: RepositoryFile) {
            this.item = item
            with(itemView) {
                projectFileIcon.setImageResource(
                    when (item.nodeType) {
                        RepositoryTreeNodeType.BLOB -> R.drawable.ic_file
                        RepositoryTreeNodeType.TREE -> R.drawable.ic_folder
                    }
                )
                projectFileName.text = item.name
                projectFileCommitMessage.text = item.commitMessage
                projectFileAuthoredDate.text = item.authoredDate.humanTime(resources)
            }
        }
    }
}
package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_merge_request_change.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.ui.global.GitDiffViewController

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */
class MergeRequestChangeAdapterDelegate(
    private val clickListener: (MergeRequestChange) -> Unit
) : AdapterDelegate<MutableList<MergeRequestChange>>() {

    override fun isForViewType(items: MutableList<MergeRequestChange>, position: Int) = true

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_merge_request_change))

    override fun onBindViewHolder(
        items: MutableList<MergeRequestChange>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position])

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        super.onViewRecycled(viewHolder)
        (viewHolder as ViewHolder).recycle()
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: MergeRequestChange

        init {
            view.setOnClickListener { clickListener(item) }
        }

        private val gitDiffViewController: GitDiffViewController = GitDiffViewController(itemView.changeFile)

        fun bind(item: MergeRequestChange) {
            this.item = item
            with(itemView) {
                changePath.text = item.newPath
                changeIcon.setImageResource(
                    when {
                        item.newFile -> R.drawable.ic_file_added
                        item.deletedFile -> R.drawable.ic_file_deleted
                        else -> R.drawable.ic_file_changed
                    }
                )
                changeFileName.text = item.newPath.let {
                    val index = it.lastIndexOf("/")
                    it.substring(if (index != -1) index + 1 else 0)
                }
                gitDiffViewController.setText(item.diff)
                changeAddedCount.text = context.getString(
                    R.string.merge_request_changes_added_count,
                    gitDiffViewController.getAddedLineCount()
                )
                changeDeletedCount.text = context.getString(
                    R.string.merge_request_changes_deleted_count,
                    gitDiffViewController.getDeletedLineCount()
                )
            }
        }

        fun recycle() {
            gitDiffViewController.release()
        }
    }
}
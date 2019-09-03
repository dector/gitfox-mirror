package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_merge_request_change.*
import kotlinx.android.synthetic.main.item_merge_request_change.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange
import ru.terrakok.gitlabclient.util.extractFileNameFromPath
import ru.terrakok.gitlabclient.util.inflate

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */

fun MergeRequestChange.isSame(other: MergeRequestChange) =
    diff == other.diff

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

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var item: MergeRequestChange

        init {
            containerView.setOnClickListener { clickListener(item) }
        }

        fun bind(item: MergeRequestChange) {
            this.item = item
            changePath.text = item.newPath
            changeIcon.setImageResource(
                when {
                    item.newFile -> R.drawable.ic_file_added
                    item.deletedFile -> R.drawable.ic_file_deleted
                    else -> R.drawable.ic_file_changed
                }
            )
            changeFileName.text = item.newPath.extractFileNameFromPath()
            gitDiffView.setGitDiffText(item.diff)
            changeAddedCount.text = changeAddedCount.context.getString(
                R.string.merge_request_changes_added_count,
                gitDiffView.getAddedLineCount()
            )
            changeDeletedCount.text = changeAddedCount.context.getString(
                R.string.merge_request_changes_deleted_count,
                gitDiffView.getDeletedLineCount()
            )
        }

        fun recycle() {
            containerView.gitDiffView.release()
        }
    }
}
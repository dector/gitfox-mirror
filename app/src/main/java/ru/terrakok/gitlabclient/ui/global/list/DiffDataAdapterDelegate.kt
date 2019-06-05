package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_diff_data.*
import kotlinx.android.synthetic.main.item_diff_data.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.DiffData
import ru.terrakok.gitlabclient.extension.extractFileNameFromPath
import ru.terrakok.gitlabclient.extension.inflate

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */

fun DiffData.isSame(other: DiffData) =
        diff == other.diff

class DiffDataAdapterDelegate(
    private val clickListener: (DiffData) -> Unit
) : AdapterDelegate<MutableList<DiffData>>() {

    override fun isForViewType(items: MutableList<DiffData>, position: Int) = true

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_diff_data))

    override fun onBindViewHolder(
        items: MutableList<DiffData>,
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

        private lateinit var item: DiffData

        init {
            containerView.setOnClickListener { clickListener(item) }
        }

        fun bind(item: DiffData) {
            this.item = item
            diffDataPath.text = item.newPath
            diffDataIcon.setImageResource(
                when {
                    item.newFile -> R.drawable.ic_file_added
                    item.deletedFile -> R.drawable.ic_file_deleted
                    else -> R.drawable.ic_file_changed
                }
            )
            diffDataFileName.text = item.newPath.extractFileNameFromPath()
            diffDataDiffView.setGitDiffText(item.diff)
            diffDataAddedCount.text = diffDataAddedCount.context.getString(
                R.string.item_diff_data_added_count,
                diffDataDiffView.getAddedLineCount()
            )
            diffDataDeletedCount.text = diffDataDeletedCount.context.getString(
                R.string.item_diff_data_deleted_count,
                diffDataDiffView.getDeletedLineCount()
            )
        }

        fun recycle() {
            containerView.diffDataDiffView.release()
        }
    }
}
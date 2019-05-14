package ru.terrakok.gitlabclient.ui.projects

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressItem
import ru.terrakok.gitlabclient.ui.global.list.ProjectAdapterDelegate

class ProjectsAdapter(
    private val nextPageListener: () -> Unit,
    private val onClickListener: (Long) -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {
    init {
        items = mutableListOf()
        delegatesManager.addDelegate(ProjectAdapterDelegate { onClickListener(it.id) })
        delegatesManager.addDelegate(ProgressAdapterDelegate())
    }

    fun setData(projects: List<Project>) {
        val progress = isProgress()
        val oldItems = items.toList()

        items.clear()
        items.addAll(projects)
        if (progress) items.add(ProgressItem())

        DiffUtil
            .calculateDiff(DiffCallback(items, oldItems), false)
            .dispatchUpdatesTo(this)
    }

    fun showProgress(isVisible: Boolean) {
        val progress = isProgress()

        if (isVisible && !progress) {
            items.add(ProgressItem())
            notifyItemInserted(items.lastIndex)
        } else if (!isVisible && progress) {
            items.remove(items.last())
            notifyItemRemoved(items.size)
        }
    }

    private fun isProgress() = items.isNotEmpty() && items.last() is ProgressItem

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (position == items.size - 10) nextPageListener()
    }

    private class DiffCallback(
        private val newItems: List<Any>,
        private val oldItems: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is Project && oldItem is Project) {
                newItem.id == oldItem.id
            } else {
                newItem is ProgressItem && oldItem is ProgressItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is Project && oldItem is Project) {
                newItem == oldItem
            } else {
                true
            }
        }
    }

}
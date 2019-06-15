package ru.terrakok.gitlabclient.ui.project.milestones

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.ui.global.list.MilestonesAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressItem

/**
 * @author Valentin Logvinovitch (glvvl) on 17.12.18.
 */
class MilestonesAdapter(
    clickListener: (Milestone) -> Unit,
    private val nextPageListener: () -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(MilestonesAdapterDelegate(clickListener))
        delegatesManager.addDelegate(ProgressAdapterDelegate())
    }

    fun setData(events: List<Milestone>) {
        val oldData = items.toList()
        val progress = isProgress()

        items.clear()
        items.addAll(events)
        if (progress) items.add(ProgressItem())

        // Yes, on main thread...
        DiffUtil
            .calculateDiff(DiffCallback(items, oldData), false)
            .dispatchUpdatesTo(this)
    }

    fun showProgress(isVisible: Boolean) {
        val oldData = items.toList()
        val currentProgress = isProgress()

        if (isVisible && !currentProgress) {
            items.add(ProgressItem())
            notifyItemInserted(items.lastIndex)
        } else if (!isVisible && currentProgress) {
            items.remove(items.last())
            notifyItemRemoved(oldData.lastIndex)
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

    private inner class DiffCallback(
        private val newItems: List<Any>,
        private val oldItems: List<Any>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is Milestone && oldItem is Milestone) {
                newItem.id == oldItem.id && newItem.createdAt == oldItem.createdAt
            } else {
                newItem is ProgressItem && oldItem is ProgressItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is Milestone && oldItem is Milestone) {
                newItem == oldItem
            } else {
                true
            }
        }
    }
}
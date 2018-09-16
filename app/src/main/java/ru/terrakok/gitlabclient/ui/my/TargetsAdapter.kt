package ru.terrakok.gitlabclient.ui.my

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressItem
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderAdapterDelegate

class TargetsAdapter(
    userClickListener: (Long) -> Unit,
    clickListener: (TargetHeader) -> Unit,
    private val nextListener: () -> Unit
) : ListDelegationAdapter<MutableList<Any>>() {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(TargetHeaderAdapterDelegate(userClickListener, clickListener))
        delegatesManager.addDelegate(ProgressAdapterDelegate())
    }

    fun setData(events: List<TargetHeader>) {
        val oldData = items.toList()
        val progress = isProgress()

        items.clear()
        items.addAll(events)
        if (progress) items.add(ProgressItem())

        //yes, on main thread...
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

        if (position == items.size - 10) nextListener()
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

            return if (newItem is TargetHeader && oldItem is TargetHeader) {
                newItem.target == oldItem.target && newItem.targetId == oldItem.targetId
                        && newItem.date == oldItem.date
            } else {
                newItem is ProgressItem && oldItem is ProgressItem
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (newItem is TargetHeader && oldItem is TargetHeader) {
                newItem == oldItem
            } else {
                true
            }
        }
    }
}
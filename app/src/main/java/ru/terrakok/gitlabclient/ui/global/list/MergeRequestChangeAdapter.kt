package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.util.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestChange

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */
class MergeRequestChangeAdapter : ListDelegationAdapter<MutableList<MergeRequestChange>>() {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(MergeRequestChangeAdapterDelegate())
    }

    fun setData(data: List<MergeRequestChange>) {
        val oldItems = items.toList()

        items.clear()
        items.addAll(data)

        DiffUtil
            .calculateDiff(DiffCallback(items, oldItems), false)
            .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
        private val newItems: List<MergeRequestChange>,
        private val oldItems: List<MergeRequestChange>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return newItem.diff == oldItem.diff
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return newItem == oldItem
        }
    }
}
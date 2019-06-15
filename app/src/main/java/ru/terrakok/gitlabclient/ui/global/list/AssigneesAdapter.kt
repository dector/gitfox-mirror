package ru.terrakok.gitlabclient.ui.global.list

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.terrakok.gitlabclient.entity.ShortUser

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
class AssigneesAdapter(
    clickListener: (ShortUser) -> Unit
) : ListDelegationAdapter<MutableList<ShortUser>>() {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(AssigneesAdapterDelegate(clickListener))
    }

    fun setData(assignees: List<ShortUser>) {
        val oldData = items.toList()

        items.clear()
        items.addAll(assignees)

        // Yes, on main thread...
        DiffUtil
            .calculateDiff(DiffCallback(items, oldData), false)
            .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
        private val newItems: List<ShortUser>,
        private val oldItems: List<ShortUser>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return newItem == oldItem
        }
    }
}
package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.util.DiffUtil
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody

class TargetNotesAdapter : ListDelegationAdapter<MutableList<NoteWithFormattedBody>>() {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(UserNoteAdapterDelegate({}))
        delegatesManager.addDelegate(SystemNoteAdapterDelegate({}))
    }

    fun setData(data: List<NoteWithFormattedBody>) {
        val oldItems = items.toList()

        items.clear()
        items.addAll(data)

        DiffUtil
                .calculateDiff(DiffCallback(items, oldItems), false)
                .dispatchUpdatesTo(this)
    }

    private inner class DiffCallback(
        private val newItems: List<NoteWithFormattedBody>,
        private val oldItems: List<NoteWithFormattedBody>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newItems[newItemPosition].note.id == oldItems[oldItemPosition].note.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                newItems[newItemPosition] == oldItems[oldItemPosition]
    }
}
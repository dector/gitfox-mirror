package ru.terrakok.gitlabclient.ui.global.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.terrakok.gitlabclient.presentation.global.NoteWithFormattedBody

fun NoteWithFormattedBody.isSame(other: NoteWithFormattedBody) =
    note.id == other.note.id

class TargetNotesAdapter : AsyncListDifferDelegationAdapter<Any>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is NoteWithFormattedBody && newItem is NoteWithFormattedBody) {
                oldItem.isSame(newItem)
            } else false
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
        override fun getChangePayload(oldItem: Any, newItem: Any) = Any()
    }
) {

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(UserNoteAdapterDelegate())
        delegatesManager.addDelegate(SystemNoteAdapterDelegate())
    }
}
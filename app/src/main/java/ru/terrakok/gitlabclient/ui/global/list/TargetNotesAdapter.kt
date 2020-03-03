package ru.terrakok.gitlabclient.ui.global.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import moxy.MvpDelegate
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId

fun NoteWithProjectId.isSame(other: NoteWithProjectId) =
    note.id == other.note.id

class TargetNotesAdapter(
    mvpDelegate: MvpDelegate<*>
) : AsyncListDifferDelegationAdapter<Any>(
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is NoteWithProjectId && newItem is NoteWithProjectId) {
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
        delegatesManager.addDelegate(UserNoteAdapterDelegate(mvpDelegate))
        delegatesManager.addDelegate(SystemNoteAdapterDelegate(mvpDelegate))
    }
}

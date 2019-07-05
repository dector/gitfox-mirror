package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user_note.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId
import ru.terrakok.gitlabclient.ui.global.view.custom.bindShortUser

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class UserNoteAdapterDelegate(
    val mvpDelegate: MvpDelegate<*>
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        with(items[position]) { this is NoteWithProjectId && !this.note.isSystem }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_user_note))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as NoteWithProjectId)

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(data: NoteWithProjectId) {
            val note = data.note
            avatarImageView.bindShortUser(note.author)
            titleTextView.text = note.author.name
            subtitleTextView.text = note.createdAt.humanTime(subtitleTextView.context.resources)
            descriptionTextView.initWithParentDelegate(mvpDelegate)
            descriptionTextView.setMarkdown(note.body, data.projectId)
        }
    }
}
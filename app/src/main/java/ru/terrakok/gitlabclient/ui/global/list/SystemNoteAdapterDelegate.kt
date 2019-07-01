package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_system_note.*
import ru.noties.markwon.Markwon
import com.arellomobile.mvp.MvpDelegate
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.presentation.global.NoteWithProjectId

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class SystemNoteAdapterDelegate(
    val mvpDelegate: MvpDelegate<*>
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        with(items[position]) { this is NoteWithProjectId && this.note.isSystem }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_system_note))

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
            titleTextView.text = data.note.author.name
            subtitleTextView.initWithParentDelegate(mvpDelegate)
            subtitleTextView.setMarkdown(note.body, data.projectId)
        }
    }
}
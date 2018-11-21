package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_merge_request_commit.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.CommitWithAuthor
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.extension.loadRoundedImage

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
class CommitAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is CommitWithAuthor

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_merge_request_commit))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as CommitWithAuthor)

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var commitWithAuthor: CommitWithAuthor

        fun bind(commitWithAuthor: CommitWithAuthor) {
            this.commitWithAuthor = commitWithAuthor
            with(itemView) {
                avatarImageView.loadRoundedImage(commitWithAuthor.authorAvatarUrl)
                titleTextView.text = commitWithAuthor.commit.title
                descriptionTextView.text = String.format(
                    context.getString(R.string.merge_request_commits_description),
                    commitWithAuthor.commit.authorName,
                    commitWithAuthor.commit.authoredDate.humanTime(resources)
                )
            }
        }
    }
}
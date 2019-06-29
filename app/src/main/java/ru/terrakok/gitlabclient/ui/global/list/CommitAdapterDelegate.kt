package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_merge_request_commit.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.CommitWithShortUser
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.ui.global.view.custom.bindShortUser

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */

fun CommitWithShortUser.isSame(other: CommitWithShortUser) = commit.id == other.commit.id

class CommitAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is CommitWithShortUser

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_merge_request_commit))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as CommitWithShortUser)

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var commitWithShortUser: CommitWithShortUser

        fun bind(commitWithShortUser: CommitWithShortUser) {
            this.commitWithShortUser = commitWithShortUser
            with(itemView) {
                val shortUser = commitWithShortUser.shortUser
                if (shortUser != null) {
                    avatarImageView.bindShortUser(shortUser)
                }
                titleTextView.text = commitWithShortUser.commit.title
                descriptionTextView.text = String.format(
                    context.getString(R.string.merge_request_commits_description),
                    commitWithShortUser.commit.authorName,
                    commitWithShortUser.commit.authoredDate.humanTime(resources)
                )
            }
        }
    }
}
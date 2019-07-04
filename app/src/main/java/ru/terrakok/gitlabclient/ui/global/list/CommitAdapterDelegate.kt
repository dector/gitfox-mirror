package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_merge_request_commit.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.inflate

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */

fun CommitWithShortUser.isSame(other: CommitWithShortUser) = commit.id == other.commit.id

class CommitAdapterDelegate(
    private val clickListener: (String) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

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

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var commitWithShortUser: CommitWithShortUser

        init {
            containerView.setOnClickListener { clickListener(commitWithShortUser.commit.id) }
        }

        fun bind(commitWithShortUser: CommitWithShortUser) {
            this.commitWithShortUser = commitWithShortUser
            commitWithShortUser.shortUser?.let { avatarImageView.bindShortUser(it) }
            titleTextView.text = commitWithShortUser.commit.title
            descriptionTextView.text = String.format(
                descriptionTextView.context.getString(R.string.merge_request_commits_description),
                commitWithShortUser.commit.authorName,
                commitWithShortUser.commit.authoredDate.humanTime(descriptionTextView.resources)
            )
        }
    }
}
package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_issue.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.target.issue.Issue
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class IssueAdapterDelegate(private val clickListener: (Issue) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.IssueItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            IssueViewHolder(parent.inflate(R.layout.item_issue), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as IssueViewHolder).bind((items[position] as ListItem.IssueItem).issue)

    private class IssueViewHolder(val view: View, clickListener: (Issue) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var issue: Issue

        init {
            view.setOnClickListener { clickListener.invoke(issue) }
        }

        fun bind(issue: Issue) {
            this.issue = issue

            val res = view.resources
            view.issueTitleTextView.text = res.getString(R.string.by_author, issue.author?.username ?: issue.author?.name)
            view.issueDescriptionTextView.text = issue.title
            view.issueIdTextView.text = res.getString(R.string.sharp_id, issue.id)
            view.issueMessagesCountTextView.text = issue.userNotesCount.toString()
            view.issueDateTextView.text = issue.createdAt?.humanTime(res)
        }
    }
}
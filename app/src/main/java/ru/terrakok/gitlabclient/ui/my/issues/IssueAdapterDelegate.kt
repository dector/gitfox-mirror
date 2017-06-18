package ru.terrakok.gitlabclient.ui.my.issues

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_issue.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.common.Issue
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.ui.global.list.ListItem

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class IssueAdapterDelegate(private val clickListener: (Issue) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.IssueItem

    override fun onCreateViewHolder(parent: ViewGroup) =
            IssueViewHolder(parent.inflate(R.layout.item_issue), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as IssueViewHolder).bind((items[position] as ListItem.IssueItem).issue)

    class IssueViewHolder(val view: View, clickListener: (Issue) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var issue: Issue

        init {
            view.setOnClickListener { clickListener.invoke(issue) }
        }

        fun bind(issue: Issue) {
            this.issue = issue
            view.issueTitle.text = issue.title
        }
    }
}
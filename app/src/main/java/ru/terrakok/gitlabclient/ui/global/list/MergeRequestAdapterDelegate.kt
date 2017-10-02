package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_merge_request.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate

class MergeRequestAdapterDelegate(private val clickListener: (MergeRequest) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.MergeRequestItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            MergeRequestViewHolder(parent.inflate(R.layout.item_merge_request), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as MergeRequestViewHolder).bind((items[position] as ListItem.MergeRequestItem).mergeRequest)

    private class MergeRequestViewHolder(val view: View, clickListener: (MergeRequest) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var mergeRequest: MergeRequest

        init {
            view.setOnClickListener { clickListener(mergeRequest) }
        }

        fun bind(mergeRequest: MergeRequest) {
            this.mergeRequest = mergeRequest

            val res = view.resources
            view.mergeRequestTitleTextView.text = mergeRequest.title
            view.mergeRequestDescriptionTextView.text = mergeRequest.description
            view.mergeRequestIdTextView.text = res.getString(R.string.sharp_id, mergeRequest.iid)
            view.mergeRequestMessagesCountTextView.text = mergeRequest.userNotesCount.toString()
            val authorName = mergeRequest.author?.name ?: mergeRequest.author?.username
            val openedAt = mergeRequest.createdAt?.humanTime(res)
            view.mergeRequestOpenedAtByAuthorTextView.text = res.getString(R.string.merge_request_item_opened_at_by_author, openedAt, authorName)
            view.mergeRequestUpdatedAtTextView.text = mergeRequest.updatedAt?.humanTime(res)
        }
    }
}
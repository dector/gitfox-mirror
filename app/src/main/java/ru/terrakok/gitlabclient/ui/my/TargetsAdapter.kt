package ru.terrakok.gitlabclient.ui.my

import android.support.v7.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.ui.global.list.ListItem
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderAdapterDelegate

class TargetsAdapter(
        clickListener: (TargetHeader) -> Unit,
        private val nextListener: () -> Unit
) : ListDelegationAdapter<MutableList<ListItem>>() {
    init {
        items = mutableListOf()
        delegatesManager.addDelegate(TargetHeaderAdapterDelegate(clickListener))
        delegatesManager.addDelegate(ProgressAdapterDelegate())
    }

    fun setData(events: List<TargetHeader>) {
        val progress = isProgress()

        items.clear()
        items.addAll(events.map { ListItem.TargetHeaderItem(it) })
        if (progress) items.add(ListItem.ProgressItem())

        notifyDataSetChanged()
    }

    fun showProgress(isVisible: Boolean) {
        val currentProgress = isProgress()

        if (isVisible && !currentProgress) items.add(ListItem.ProgressItem())
        else if (!isVisible && currentProgress) items.remove(items.last())

        notifyDataSetChanged()
    }

    private fun isProgress() = items.isNotEmpty() && items.last() is ListItem.ProgressItem

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
        super.onBindViewHolder(holder, position, payloads)

        if (position == items.size - 10) nextListener()
    }
}
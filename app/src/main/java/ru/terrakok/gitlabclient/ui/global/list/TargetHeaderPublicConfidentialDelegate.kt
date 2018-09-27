package ru.terrakok.gitlabclient.ui.global.list

import ru.terrakok.gitlabclient.R
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.inflate

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 28.10.18.
 */
class TargetHeaderPublicConfidentialDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is TargetHeader.Confidential

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_target_header_confidential))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as TargetHeader.Confidential)

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: TargetHeader.Confidential

        fun bind(item: TargetHeader.Confidential) {
            this.item = item
        }
    }
}
package ru.terrakok.gitlabclient.ui.project.labels

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_label.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.12.2018.
 */
class ProjectLabelAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return parent.inflate(R.layout.item_label)
            .let(::Holder)
    }

    override fun isForViewType(items: MutableList<Any>, position: Int): Boolean {
        return items[position] is Label
    }

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        holder as Holder
        val item = items[position] as Label
        holder.bind(item)
    }

    private class Holder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Label) {
            with(itemView) {
                labelTitleTextView.text = item.name
            }
        }
    }

}
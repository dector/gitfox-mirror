package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_target_header.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.extension.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class TargetHeaderAdapterDelegate(
        private val clickListener: (TargetHeader) -> Unit
) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.TargetHeaderItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            TargetHeaderViewHolder(parent.inflate(R.layout.item_target_header), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as TargetHeaderViewHolder).bind((items[position] as ListItem.TargetHeaderItem).item)

    private class TargetHeaderViewHolder(val view: View, clickListener: (TargetHeader) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var item: TargetHeader

        init {
            view.setOnClickListener { clickListener(item) }
        }

        fun bind(item: TargetHeader) {
            this.item = item

            val res = view.resources
            view.titleTextView.text = item.title.getHumanName(res)
            view.descriptionTextView.text = item.body
            view.avatarImageView.loadRoundedImage(item.author.avatarUrl)
            view.iconImageView.setImageResource(item.icon.getIcon())
            view.dateTextView.text = item.date.humanTime(res)

            view.descriptionTextView.visible(item.body != null)
            view.iconImageView.visible(item.icon != TargetHeaderIcon.NONE)
        }
    }
}
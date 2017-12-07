package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_event.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.FullEventInfo
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class EventAdapterDelegate(private val clickListener: (FullEventInfo) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.EventItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            EventViewHolder(parent.inflate(R.layout.item_event), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as EventViewHolder).bind((items[position] as ListItem.EventItem).event)

    private class EventViewHolder(val view: View, clickListener: (FullEventInfo) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var event: FullEventInfo

        init {
            view.setOnClickListener { clickListener.invoke(event) }
        }

        fun bind(event: FullEventInfo) {
            this.event = event

            view.eventTitleTextView.text = event.action.name
        }
    }
}
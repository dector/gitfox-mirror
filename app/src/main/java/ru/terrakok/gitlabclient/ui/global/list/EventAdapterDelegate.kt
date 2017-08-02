package ru.terrakok.gitlabclient.ui.global.list

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_event.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class EventAdapterDelegate(private val clickListener: (Event) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.EventItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            EventViewHolder(parent.inflate(R.layout.item_event), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as EventViewHolder).bind((items[position] as ListItem.EventItem).event)

    private class EventViewHolder(val view: View, clickListener: (Event) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var event: Event

        init {
            view.setOnClickListener { clickListener.invoke(event) }
        }

        fun bind(event: Event) {
            this.event = event

            val res = view.resources
            view.eventTitleTextView.text = event.authorUsername
            view.eventDateTextView.text = event.createdAt?.humanTime(res)
            view.eventDescriptionTextView.text = createMessage(event)
            view.eventSubtitleTextView.text = "#${event.targetId}"

            Glide.with(view.context)
                    .load(event.author?.avatarUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(object : BitmapImageViewTarget(view.eventAvatarImageView) {
                        override fun setResource(resource: Bitmap?) {
                            resource?.let {
                                RoundedBitmapDrawableFactory.create(view.resources, it).run {
                                    this.isCircular = true
                                    view.eventAvatarImageView.setImageDrawable(this)
                                }
                            }
                        }
                    })
        }

        private fun createMessage(event: Event): String {
            if (event.targetType != null) {
                return "${event.actionName} ${event.targetType}: ${event.targetTitle}"
            } else {
                return event.actionName
            }
        }
    }
}
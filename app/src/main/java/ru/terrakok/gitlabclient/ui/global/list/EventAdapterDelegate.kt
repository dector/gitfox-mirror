package ru.terrakok.gitlabclient.ui.global.list

import android.content.res.Resources
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
import ru.terrakok.gitlabclient.entity.app.FullEventInfo
import ru.terrakok.gitlabclient.extension.getHumanName
import ru.terrakok.gitlabclient.extension.getIcon
import ru.terrakok.gitlabclient.extension.humanTime
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

            val res = view.resources
            view.eventTitleTextView.text = event.author.username
            view.eventDateTextView.text = event.createdAt.humanTime(res)
            view.eventSubtitleTextView.text = createSubtitle(res, event)
            view.eventDescriptionTextView.text = event.body
            view.eventIconImageView.setImageResource(event.action.getIcon())

            Glide.with(view.context)
                    .load(event.author.avatarUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(object : BitmapImageViewTarget(view.eventAvatarImageView) {
                        override fun setResource(resource: Bitmap?) {
                            resource?.let {
                                RoundedBitmapDrawableFactory.create(res, it).run {
                                    this.isCircular = true
                                    view.eventAvatarImageView.setImageDrawable(this)
                                }
                            }
                        }
                    })
        }

        private fun createSubtitle(resources: Resources, event: FullEventInfo): String {
            val actionName = event.action.getHumanName(resources)
            val target = event.target.getHumanName(resources)
            val targetId = event.targetId

            return "$actionName $target #$targetId"
        }
    }
}
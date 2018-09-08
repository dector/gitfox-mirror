package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_target_badge.view.*
import kotlinx.android.synthetic.main.item_target_header.view.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetBadge
import ru.terrakok.gitlabclient.entity.app.target.TargetBadgeIcon
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.extension.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class TargetHeaderAdapterDelegate(
    private val avatarClickListener: (Long) -> Unit,
    private val clickListener: (TargetHeader) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is TargetHeader

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_target_header))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as TargetHeader)

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)

        (holder as ViewHolder).release()
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        super.onViewRecycled(viewHolder)

        (viewHolder as ViewHolder).release()
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var item: TargetHeader

        init {
            view.setOnClickListener { clickListener(item) }
            view.avatarImageView.setOnClickListener { avatarClickListener(item.author.id) }

            (1..5).forEach {
                view.badgesContainer.inflate(R.layout.item_target_badge, true)
            }
        }

        fun bind(item: TargetHeader) {
            this.item = item
            with(itemView) {
                titleTextView.text = item.title.getHumanName(resources)
                Markwon.setText(descriptionTextView, item.body ?: "")
                descriptionTextView.movementMethod = null //disable internal link click
                avatarImageView.loadRoundedImage(item.author.avatarUrl)
                iconImageView.setImageResource(item.icon.getIcon())
                dateTextView.text = item.date.humanTime(resources)

                descriptionTextView.visible(item.body != null)
                iconImageView.visible(item.icon != TargetHeaderIcon.NONE)

                bindBadges(item.badges)
            }
        }

        private fun bindBadges(badges: List<TargetBadge>) {
            with(itemView) {
                commentsTextView.visible(false)
                commitsTextView.visible(false)
                upVotesTextView.visible(false)
                downVotesTextView.visible(false)

                val badgeViewsCount = badgesContainer.childCount
                val textBadgesCount = badges.count { it !is TargetBadge.Icon }
                if (textBadgesCount > badgeViewsCount) {
                    (1..textBadgesCount - badgeViewsCount).forEach {
                        badgesContainer.inflate(R.layout.item_target_badge, true)
                    }
                }

                (0 until badgesContainer.childCount).forEach { i ->
                    badgesContainer.getChildAt(i).visible(false)
                }

                var i = 0
                badges.forEach { badge ->
                    when (badge) {
                        is TargetBadge.Text -> {
                            val badgeView = badgesContainer.getChildAt(i) as TextView
                            badgeView.textTextView.text = badge.text
                            badgeView.textTextView.setTextColor(context.color(R.color.colorPrimary))
                            badgeView.textTextView.setBackgroundColor(context.color(R.color.colorPrimaryLight))
                            badgeView.visible(true)
                            i++
                        }
                        is TargetBadge.Status -> {
                            val badgeView = badgesContainer.getChildAt(i) as TextView
                            badgeView.textTextView.text = badge.status.getHumanName(resources)
                            val (textColor, bgColor) = badge.status.getBadgeColors(context)
                            badgeView.textTextView.setTextColor(textColor)
                            badgeView.textTextView.setBackgroundColor(bgColor)
                            badgeView.visible(true)
                            i++
                        }
                        is TargetBadge.Icon -> {
                            if (badge.count > 0) {
                                when (badge.icon) {
                                    TargetBadgeIcon.COMMENTS -> {
                                        commentsTextView.text = badge.count.toString()
                                        commentsTextView.visible(true)
                                    }
                                    TargetBadgeIcon.COMMITS -> {
                                        commitsTextView.text = badge.count.toString()
                                        commitsTextView.visible(true)
                                    }
                                    TargetBadgeIcon.UP_VOTES -> {
                                        upVotesTextView.text = badge.count.toString()
                                        upVotesTextView.visible(true)
                                    }
                                    TargetBadgeIcon.DOWN_VOTES -> {
                                        downVotesTextView.text = badge.count.toString()
                                        downVotesTextView.visible(true)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        fun release() = Markwon.unscheduleDrawables(itemView.descriptionTextView)
    }
}
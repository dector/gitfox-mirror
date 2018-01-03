package ru.terrakok.gitlabclient.ui.global.list

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_target_badge.view.*
import kotlinx.android.synthetic.main.item_target_header.view.*
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

            (1..5).forEach {
                view.badgesContainer.inflate(R.layout.item_target_badge, true)
            }
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

            bindBadges(item.badges, res)
        }

        private fun bindBadges(badges: List<TargetBadge>, res: Resources) {
            view.commentsTextView.visible(false)
            view.commitsTextView.visible(false)
            view.upVotesTextView.visible(false)
            view.downVotesTextView.visible(false)

            val badgeViewsCount = view.badgesContainer.childCount
            val textBadgesCount = badges.count { it !is TargetBadge.Icon }
            if (textBadgesCount > badgeViewsCount) {
                (1..textBadgesCount - badgeViewsCount).forEach {
                    view.badgesContainer.inflate(R.layout.item_target_badge, true)
                }
            }

            (0 until view.badgesContainer.childCount).forEach { i ->
                view.badgesContainer.getChildAt(i).visible(false)
            }

            var i = 0
            badges.forEach { badge ->
                when (badge) {
                    is TargetBadge.Text -> {
                        val badgeView = view.badgesContainer.getChildAt(i) as TextView
                        badgeView.textTextView.text = badge.text
                        badgeView.textTextView.setTextColor(res.color(R.color.colorPrimary))
                        badgeView.textTextView.setBackgroundColor(res.color(R.color.colorPrimaryLight))
                        badgeView.visible(true)
                        i++
                    }
                    is TargetBadge.Status -> {
                        val badgeView = view.badgesContainer.getChildAt(i) as TextView
                        badgeView.textTextView.text = badge.status.getHumanName(res)
                        val (textColor, bgColor) = badge.status.getBadgeColors(res)
                        badgeView.textTextView.setTextColor(textColor)
                        badgeView.textTextView.setBackgroundColor(bgColor)
                        badgeView.visible(true)
                        i++
                    }
                    is TargetBadge.Icon -> {
                        if (badge.count > 0) {
                            when (badge.icon) {
                                TargetBadgeIcon.COMMENTS -> {
                                    view.commentsTextView.text = badge.count.toString()
                                    view.commentsTextView.visible(true)
                                }
                                TargetBadgeIcon.COMMITS -> {
                                    view.commitsTextView.text = badge.count.toString()
                                    view.commitsTextView.visible(true)
                                }
                                TargetBadgeIcon.UP_VOTES -> {
                                    view.upVotesTextView.text = badge.count.toString()
                                    view.upVotesTextView.visible(true)
                                }
                                TargetBadgeIcon.DOWN_VOTES -> {
                                    view.downVotesTextView.text = badge.count.toString()
                                    view.downVotesTextView.visible(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
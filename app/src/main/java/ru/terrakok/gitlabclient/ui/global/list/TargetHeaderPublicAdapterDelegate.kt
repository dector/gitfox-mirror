package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_target_badge.view.*
import kotlinx.android.synthetic.main.item_target_header_public.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetBadge
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.extension.*
import ru.terrakok.gitlabclient.ui.global.view.custom.bindShortUser

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */

fun TargetHeader.Public.isSame(other: TargetHeader.Public) =
    target == other.target
            && targetId == other.targetId
            && date == other.date

class TargetHeaderPublicAdapterDelegate(
    private val clickListener: (TargetHeader.Public) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is TargetHeader.Public

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_target_header_public))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as TargetHeader.Public)

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var item: TargetHeader.Public

        init {
            containerView.setOnClickListener { clickListener(item) }

            badgesContainer.prepare(R.layout.item_target_badge, 5)
            iconsContainer.prepare(R.layout.item_target_icon, 5)
        }

        fun bind(item: TargetHeader.Public) {
            this.item = item
            titleTextView.text = item.title.getHumanName(titleTextView.resources)
            Markwon.setText(descriptionTextView, item.body)
            descriptionTextView.movementMethod = null // Disable internal link click
            avatarImageView.bindShortUser(item.author)
            iconImageView.setImageResource(item.icon.getIcon())
            dateTextView.text = item.date.humanTime(dateTextView.resources)

            descriptionTextView.visible(item.body.isNotEmpty())
            iconImageView.visible(item.icon != TargetHeaderIcon.NONE)

            bindBadges(item.badges)
        }

        private fun bindBadges(badges: List<TargetBadge>) {
            val textBadgesCount = badges.count { it !is TargetBadge.Icon }
            badgesContainer.prepare(R.layout.item_target_badge, textBadgesCount)

            val iconBadgesCount = badges.size - textBadgesCount
            iconsContainer.prepare(R.layout.item_target_icon, iconBadgesCount)

            var textBadgesCounter = 0
            var iconBadgesCounter = 0
            badges.forEach { badge ->
                when (badge) {
                    is TargetBadge.Text -> {
                        val badgeView = badgesContainer.getChildAt(textBadgesCounter) as TextView
                        badgeView.textTextView.text = badge.text
                        badgeView.textTextView.setTextColor(badgeView.context.color(R.color.colorPrimary))
                        badgeView.textTextView.setBackgroundColor(badgeView.context.color(R.color.colorPrimaryLight))
                        badgeView.visible(true)
                        textBadgesCounter++
                    }
                    is TargetBadge.Status -> {
                        val badgeView = badgesContainer.getChildAt(textBadgesCounter) as TextView
                        badgeView.textTextView.text = badge.status.getHumanName(badgeView.resources)
                        val (textColor, bgColor) = badge.status.getBadgeColors(badgeView.context)
                        badgeView.textTextView.setTextColor(textColor)
                        badgeView.textTextView.setBackgroundColor(bgColor)
                        badgeView.visible(true)
                        textBadgesCounter++
                    }
                    is TargetBadge.Icon -> {
                        val iconBadgeView = iconsContainer.getChildAt(iconBadgesCounter) as TextView
                        iconBadgeView.text = badge.value
                        val iconRes = badge.icon.getIcon()
                        val colorRes = badge.icon.getColor()
                        val icon = iconBadgeView.context.getTintDrawable(iconRes, colorRes)
                        iconBadgeView.setTextColor(iconBadgeView.context.color(colorRes))
                        iconBadgeView.setStartDrawable(icon)
                        iconBadgeView.visible(true)
                        iconBadgesCounter++
                    }
                }
            }
        }

        private fun ViewGroup.prepare(@LayoutRes layoutId: Int, viewsCount: Int) {
            val viewsToInflate = viewsCount - childCount
            if (viewsToInflate > 0) {
                repeat(viewsToInflate) { inflate(layoutId, true) }
            }

            (0 until childCount).forEach { i -> getChildAt(i).visible(false) }
        }
    }
}
package ru.terrakok.gitlabclient.ui.project.labels

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import gitfox.entity.Color
import gitfox.entity.Label
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_label.*
import kotlinx.android.synthetic.main.item_label.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.util.inflate
import ru.terrakok.gitlabclient.util.setBackgroundTintByColor

/**
 * @author Maxim Myalkin (MaxMyalkin) on 15.12.2018.
 */

fun Label.isSame(other: Label) = id == other.id

class ProjectLabelAdapterDelegate : AdapterDelegate<MutableList<Any>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_label))
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
        (holder as ViewHolder).bind(items[position] as Label)
    }

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        @SuppressLint("SetTextI18n")
        fun bind(item: Label) {
            labelTitleTextView.text = item.name

            val descriptionIsEmpty = item.description.isNullOrBlank()
            labelDescriptionTextView.text = if (descriptionIsEmpty) {
                labelDescriptionTextView.context.getString(R.string.label_description_empty)
            } else {
                item.description
            }
            labelDescriptionTextView.isEnabled = descriptionIsEmpty.not()

            labelIssueCountTextView.text = "${item.closedIssuesCount + item.openIssuesCount}"
            labelMrCountTextView.text = "${item.openMergeRequestsCount}"
            setLabelColor(item.color)
        }

        private fun setLabelColor(color: Color) = with(itemView.labelTitleTextView) {
            val textColor = when {
                isColorDark(color.value) -> ContextCompat.getColor(context, R.color.white)
                else -> ContextCompat.getColor(context, R.color.primary_text)
            }

            setBackgroundTintByColor(color.value)
            setTextColor(textColor)
        }

        private fun isColorDark(color: Int) = ColorUtils.calculateLuminance(color) < 0.5f
    }
}

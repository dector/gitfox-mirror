package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_milestone.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Milestone
import ru.terrakok.gitlabclient.util.*

/**
 * @author Valentin Logvinovitch (glvvl) on 17.12.18.
 */

fun Milestone.isSame(other: Milestone) = id == other.id

class MilestonesAdapterDelegate(
    private val clickListener: (Milestone) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is Milestone

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_milestone))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as Milestone)

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private lateinit var item: Milestone

        init {
            containerView.setOnClickListener { clickListener(item) }
        }

        fun bind(item: Milestone) {
            this.item = item
            titleTextView.text = item.title
            val startDate = item.startDate
            val dueDate = item.dueDate
            if (startDate != null && dueDate != null) {
                dateTextView.text = String.format(
                    dateTextView.context.getString(R.string.project_milestone_date),
                    startDate.humanDate(),
                    dueDate.humanDate()
                )
                dateTextView.visible(true)
            } else {
                dateTextView.visible(false)
            }
            val (textColor, bgColor) = item.state.getStateColors(stateTextView.context)
            stateTextView.setTextColor(textColor)
            stateTextView.setBackgroundColor(bgColor)
            stateTextView.text = item.state.getHumanName(stateTextView.resources)
        }
    }
}

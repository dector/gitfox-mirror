package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_milestone.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Valentin Logvinovitch (glvvl) on 17.12.18.
 */
class MilestonesAdapterDelegate(
    private val clickListener: (Long) -> Unit
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

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var milestone: Milestone

        init {
            //TODO Milestone Flow(uncomment next line when Milestone Flow is ready).
            // view.setOnClickListener { clickListener(milestone.id) }
        }

        fun bind(data: Milestone) {
            this.milestone = data
            with(itemView) {
                titleTextView.text = data.title
                dateTextView.text = data.createdAt?.humanTime(resources)
            }
        }
    }
}
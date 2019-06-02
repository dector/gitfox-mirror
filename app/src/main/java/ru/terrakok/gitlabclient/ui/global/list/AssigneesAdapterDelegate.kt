package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.synthetic.main.item_assignee.view.*
import kotlinx.android.synthetic.main.item_target_header_public.view.avatarImageView
import kotlinx.android.synthetic.main.item_target_header_public.view.titleTextView
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.extension.loadRoundedImage

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
class AssigneesAdapterDelegate(
    private val clickListener: (ShortUser) -> Unit
) : AdapterDelegate<MutableList<ShortUser>>() {

    override fun isForViewType(items: MutableList<ShortUser>, position: Int) = true

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_assignee))

    override fun onBindViewHolder(
        items: MutableList<ShortUser>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (holder as ViewHolder).bind(items[position])

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: ShortUser

        init {
            view.setOnClickListener { clickListener(item) }
        }

        fun bind(item: ShortUser) {
            this.item = item
            with(itemView) {
                titleTextView.text = item.name
                subtitleTextView.text = item.username
                avatarImageView.loadRoundedImage(item.avatarUrl)
            }
        }
    }
}
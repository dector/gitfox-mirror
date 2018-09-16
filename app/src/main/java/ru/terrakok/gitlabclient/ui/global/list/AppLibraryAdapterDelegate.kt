package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_app_library.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.extension.getHumanName
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class AppLibraryAdapterDelegate(private val clickListener: (AppLibrary) -> Unit) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is AppLibrary

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_app_library))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) =
        (viewHolder as ViewHolder).bind(items[position] as AppLibrary)

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var appLibrary: AppLibrary

        init {
            view.setOnClickListener { clickListener(appLibrary) }
        }

        fun bind(appLibrary: AppLibrary) {
            this.appLibrary = appLibrary
            with(itemView) {
                titleTextView.text = appLibrary.name
                subtitleTextView.text = appLibrary.license.getHumanName(resources)
            }
        }
    }
}
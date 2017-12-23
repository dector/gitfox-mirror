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
class AppLibraryAdapterDelegate(private val clickListener: (AppLibrary) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.AppLibraryItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            AppLibraryViewHolder(parent.inflate(R.layout.item_app_library), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as AppLibraryViewHolder).bind((items[position] as ListItem.AppLibraryItem).item)

    private class AppLibraryViewHolder(val view: View, clickListener: (AppLibrary) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var appLibrary: AppLibrary

        init {
            view.setOnClickListener { clickListener.invoke(appLibrary) }
        }

        fun bind(appLibrary: AppLibrary) {
            this.appLibrary = appLibrary
            view.titleTextView.text = appLibrary.name
            view.subtitleTextView.text = appLibrary.license.getHumanName(view.resources)
        }
    }
}
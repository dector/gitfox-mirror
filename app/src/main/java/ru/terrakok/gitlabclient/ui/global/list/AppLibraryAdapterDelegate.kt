package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_app_library.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.util.getHumanName
import ru.terrakok.gitlabclient.util.inflate

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 18.06.17.
 */
class AppLibraryAdapterDelegate(private val clickListener: (AppLibrary) -> Unit) :
    AdapterDelegate<MutableList<Any>>() {

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

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        private lateinit var appLibrary: AppLibrary

        init {
            containerView.setOnClickListener { clickListener(appLibrary) }
        }

        fun bind(appLibrary: AppLibrary) {
            this.appLibrary = appLibrary
            titleTextView.text = appLibrary.name
            subtitleTextView.text = appLibrary.license.getHumanName(subtitleTextView.resources)
        }
    }
}

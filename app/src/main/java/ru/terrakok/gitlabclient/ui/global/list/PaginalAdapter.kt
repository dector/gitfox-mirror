package ru.terrakok.gitlabclient.ui.global.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

/**
 * Created by petrova_alena on 2019-12-05.
 */

class PaginalAdapter(
    private val nextPageCallback: () -> Unit,
    private val itemDiff: (old: Any, new: Any) -> Boolean,
    vararg delegate: AdapterDelegate<MutableList<Any>>
) : AsyncListDifferDelegationAdapter<Any>(
        object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem === newItem) return true
                return itemDiff.invoke(oldItem, newItem)
            }

            override fun getChangePayload(oldItem: Any, newItem: Any) = Any()

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == newItem
        }
) {
    var fullData = false

    init {
        items = mutableListOf()
        delegatesManager
                .addDelegate(ProgressAdapterDelegate())
        delegate.forEach { delegatesManager.addDelegate(it) }
    }

    fun update(data: List<Any>, isPageProgress: Boolean) {
        items = mutableListOf<Any>().apply {
            addAll(data)
            if (isPageProgress) add(ProgressItem)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any?>) {
        super.onBindViewHolder(holder, position, payloads)
        if (!fullData && position >= items.size - 10) nextPageCallback.invoke()
    }
}

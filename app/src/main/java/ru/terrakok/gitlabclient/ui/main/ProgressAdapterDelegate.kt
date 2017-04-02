package ru.terrakok.gitlabclient.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import ru.terrakok.gitlabclient.R

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class ProgressAdapterDelegate : AbsListItemAdapterDelegate<ProjectsListItem.ProgressItem, ProjectsListItem, ProgressAdapterDelegate.ProgressViewHolder>() {
    override fun isForViewType(item: ProjectsListItem, items: MutableList<ProjectsListItem>, position: Int)
            = item is ProjectsListItem.ProgressItem

    override fun onCreateViewHolder(parent: ViewGroup)
            = ProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false))

    override fun onBindViewHolder(item: ProjectsListItem.ProgressItem, viewHolder: ProgressViewHolder, payloads: MutableList<Any>) {}

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
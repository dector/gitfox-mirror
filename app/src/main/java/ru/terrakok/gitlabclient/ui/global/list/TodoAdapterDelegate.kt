package ru.terrakok.gitlabclient.ui.global.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.item_todo.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.extension.inflate

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 01.11.17
 */
class TodoAdapterDelegate(private val clickListener: (Todo) -> Unit) : AdapterDelegate<MutableList<ListItem>>() {

    override fun isForViewType(items: MutableList<ListItem>, position: Int) =
            items[position] is ListItem.TodoItem

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            TodoViewHolder(parent.inflate(R.layout.item_todo), clickListener)

    override fun onBindViewHolder(items: MutableList<ListItem>,
                                  position: Int,
                                  viewHolder: RecyclerView.ViewHolder,
                                  payloads: MutableList<Any>) =
            (viewHolder as TodoViewHolder).bind((items[position] as ListItem.TodoItem).todo)

    private class TodoViewHolder(val view: View, clickListener: (Todo) -> Unit) : RecyclerView.ViewHolder(view) {
        private lateinit var todo: Todo

        init {
            view.setOnClickListener { clickListener.invoke(todo) }
        }

        fun bind(todo: Todo) {
            this.todo = todo

            view.todoTitleTextView.text = todo.id.toString()
        }
    }
}
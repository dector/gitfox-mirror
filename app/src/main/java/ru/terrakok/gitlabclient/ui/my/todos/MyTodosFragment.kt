package ru.terrakok.gitlabclient.ui.my.todos

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodoListView
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodosPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.TodoListPendingState
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.ListItem
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TodoAdapterDelegate
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyTodosFragment : BaseFragment(), MyTodoListView {

    companion object {
        private val ARG_MODE_IS_PENDING = "arg_mode_is_pending"

        fun newInstance(isPending: Boolean) = MyTodosFragment().apply {
            arguments = Bundle().apply { putBoolean(ARG_MODE_IS_PENDING, isPending) }
        }
    }

    override val layoutRes = R.layout.fragment_my_todos
    private val adapter = TodosAdapter()

    @InjectPresenter lateinit var presenter: MyTodosPresenter

    @ProvidePresenter
    fun providePresenter(): MyTodosPresenter {
        val scopeName = "MyTodoListScope_${hashCode()}"
        val scope = Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(PrimitiveWrapper::class.java)
                        .withName(TodoListPendingState::class.java)
                        .toInstance(PrimitiveWrapper(arguments?.get(ARG_MODE_IS_PENDING)))
            }
        })

        return scope.getInstance(MyTodosPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyTodosFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshTodos() }
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        swipeToRefresh.post { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        recyclerView.post { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        // todo
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        //todo
        if (show && message != null) showSnackMessage(message)
    }

    override fun showTodos(show: Boolean, todos: List<Todo>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(todos) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    private inner class TodosAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(TodoAdapterDelegate({ presenter.onTodoClick(it) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(todos: List<Todo>) {
            val progress = isProgress()

            items.clear()
            items.addAll(todos.map { ListItem.TodoItem(it) })
            if (progress) items.add(ListItem.ProgressItem())

            notifyDataSetChanged()
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ListItem.ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ListItem.ProgressItem

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.size - 10) presenter.loadNextTodosPage()
        }
    }
}
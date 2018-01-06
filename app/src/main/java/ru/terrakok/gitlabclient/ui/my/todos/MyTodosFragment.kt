package ru.terrakok.gitlabclient.ui.my.todos

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodoListView
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodosPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.TodoListPendingState
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
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

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
                { presenter.onUserClick(it) },
                { presenter.onTodoClick(it) },
                { presenter.loadNextTodosPage() }
        )
    }
    private var zeroViewHolder: ZeroViewHolder? = null

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
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showTodos(show: Boolean, todos: List<TargetHeader>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(todos) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
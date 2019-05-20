package ru.terrakok.gitlabclient.ui.my.todos

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.TodoListPendingState
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodoListView
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodosPresenter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.my.TargetsAdapter
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyTodosFragment : BaseFragment(), MyTodoListView {

    override val layoutRes = R.layout.fragment_my_todos

    private val adapter: TargetsAdapter by lazy {
        TargetsAdapter(
            mvpDelegate,
            { presenter.onUserClick(it) },
            { presenter.onTodoClick(it) },
            { presenter.loadNextTodosPage() }
        )
    }

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(PrimitiveWrapper::class.java)
                    .withName(TodoListPendingState::class.java)
                    .toInstance(PrimitiveWrapper(arguments?.get(ARG_MODE_IS_PENDING)))
            }
        })
    }

    @InjectPresenter
    lateinit var presenter: MyTodosPresenter

    @ProvidePresenter
    fun providePresenter(): MyTodosPresenter =
        scope.getInstance(MyTodosPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@MyTodosFragment.adapter
        }

        swipeToRefresh.setOnRefreshListener { presenter.refreshTodos() }
        emptyView.setRefreshListener { presenter.refreshTodos() }
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showPageProgress(show: Boolean) {
        postViewAction { adapter.showProgress(show) }
    }

    override fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyData() else hide() }
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        emptyView.apply { if (show) showEmptyError(message) else hide() }
    }

    override fun showTodos(show: Boolean, todos: List<TargetHeader>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(todos) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    companion object {
        private const val ARG_MODE_IS_PENDING = "arg_mode_is_pending"

        fun create(isPending: Boolean) = MyTodosFragment().apply {
            arguments = Bundle().apply { putBoolean(ARG_MODE_IS_PENDING, isPending) }
        }
    }
}
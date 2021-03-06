package ru.terrakok.gitlabclient.ui.my.todos

import android.os.Bundle
import gitfox.entity.app.target.TargetHeader
import kotlinx.android.synthetic.main.fragment_my_todos.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.TodoListPendingState
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodoListView
import ru.terrakok.gitlabclient.presentation.my.todos.MyTodosPresenter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderConfidentialAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderPublicAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyTodosFragment : BaseFragment(), MyTodoListView {

    override val layoutRes = R.layout.fragment_my_todos

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

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextTodosPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate { presenter.onTodoClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshTodos() },
            adapter
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
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

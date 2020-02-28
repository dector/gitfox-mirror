package ru.terrakok.gitlabclient.ui.my.mergerequests

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_my_merge_requests.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.my.mergerequests.MyMergeRequestListView
import ru.terrakok.gitlabclient.presentation.my.mergerequests.MyMergeRequestsPresenter
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
class MyMergeRequestsFragment : BaseFragment(), MyMergeRequestListView {

    override val layoutRes = R.layout.fragment_my_merge_requests

    @InjectPresenter
    lateinit var presenter: MyMergeRequestsPresenter

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(MyMergeRequestsPresenter.Filter::class.java)
                    .toInstance(
                        MyMergeRequestsPresenter.Filter(
                            arguments?.getBoolean(ARG_MODE_CREATED_BY_ME) ?: true,
                            arguments?.getBoolean(ARG_MODE_ONLY_OPENED) ?: false
                        )
                    )
            }
        })
    }

    @ProvidePresenter
    fun providePresenter(): MyMergeRequestsPresenter =
        scope.getInstance(MyMergeRequestsPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextMergeRequestsPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate { presenter.onMergeRequestClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshMergeRequests() },
            adapter
        )
    }

    fun showOnlyOpened(onlyOpened: Boolean) {
        presenter.applyNewFilter(
            MyMergeRequestsPresenter.Filter(
                arguments?.getBoolean(ARG_MODE_CREATED_BY_ME) ?: true,
                onlyOpened
            )
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    companion object {
        private const val ARG_MODE_CREATED_BY_ME = "arg_mode_created_by_me"
        private const val ARG_MODE_ONLY_OPENED = "arg_mode_only opened"

        fun create(createdByMe: Boolean, onlyOpened: Boolean) =
            MyMergeRequestsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_MODE_CREATED_BY_ME, createdByMe)
                    putBoolean(ARG_MODE_ONLY_OPENED, onlyOpened)
                }
            }
    }
}

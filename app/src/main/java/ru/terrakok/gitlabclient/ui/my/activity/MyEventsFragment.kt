package ru.terrakok.gitlabclient.ui.my.activity

import android.os.Bundle
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_my_activity.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.my.events.MyEventsPresenter
import ru.terrakok.gitlabclient.presentation.my.events.MyEventsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderConfidentialAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.TargetHeaderPublicAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 13.06.17
 */
class MyEventsFragment : BaseFragment(), MyEventsView {
    override val layoutRes = R.layout.fragment_my_activity

    @InjectPresenter
    lateinit var presenter: MyEventsPresenter

    @ProvidePresenter
    fun providePresenter(): MyEventsPresenter =
        scope.getInstance(MyEventsPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onMenuClick() }
        toolbar.addSystemTopPadding()
        paginalRenderView.init(
            { presenter.refreshEvents() },
            { presenter.loadNextEventsPage() },
            { o, n ->
                if (o is TargetHeader.Public && n is TargetHeader.Public) {
                    o.isSame(n)
                } else false
            },
            TargetHeaderPublicAdapterDelegate { presenter.onItemClick(it) },
            TargetHeaderConfidentialAdapterDelegate()
        )
    }

    override fun renderPaginatorState(state: Paginator.State) {
        paginalRenderView.render(state)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
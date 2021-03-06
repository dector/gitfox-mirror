package ru.terrakok.gitlabclient.ui.project.members

import android.os.Bundle
import gitfox.entity.Member
import kotlinx.android.synthetic.main.fragment_project_members.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.project.members.ProjectMembersPresenter
import ru.terrakok.gitlabclient.presentation.project.members.ProjectMembersView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.MembersAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.PaginalAdapter
import ru.terrakok.gitlabclient.ui.global.list.isSame
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * @author Valentin Logvinovitch (glvvl) on 28.02.19.
 */
class ProjectMembersFragment : BaseFragment(), ProjectMembersView {

    override val layoutRes = R.layout.fragment_project_members

    @InjectPresenter
    lateinit var presenter: ProjectMembersPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectMembersPresenter =
            scope.getInstance(ProjectMembersPresenter::class.java)

    private val adapter by lazy { PaginalAdapter(
            { presenter.loadNextMembersPage() },
            { o, n ->
                if (o is Member && n is Member) {
                    o.isSame(n)
                } else false
            },
            MembersAdapterDelegate { presenter.onMemberClick(it) }
    ) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        paginalRenderView.init(
            { presenter.refreshMembers() },
            adapter
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

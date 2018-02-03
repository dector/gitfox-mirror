package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_info.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.issue.IssueInfoPresenter
import ru.terrakok.gitlabclient.presentation.issue.IssueInfoView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.02.18.
 */
class IssueInfoFragment : BaseFragment(), IssueInfoView {

    override val layoutRes = R.layout.fragment_issue_info

    @InjectPresenter lateinit var presenter: IssueInfoPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.ISSUE_SCOPE)
                .getInstance(IssueInfoPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showIssue(issueInfo: IssueInfoView.IssueInfo) {
        toolbar.title = issueInfo.issue.id.toString()
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
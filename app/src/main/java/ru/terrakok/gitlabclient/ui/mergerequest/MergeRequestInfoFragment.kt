package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_info.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestInfoPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestInfoView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.02.18.
 */
class MergeRequestInfoFragment : BaseFragment(), MergeRequestInfoView {

    override val layoutRes = R.layout.fragment_mr_info

    @InjectPresenter
    lateinit var presenter: MergeRequestInfoPresenter

    @ProvidePresenter
    fun providePresenter() =
            Toothpick.openScope(DI.MERGE_REQUEST_SCOPE)
                    .getInstance(MergeRequestInfoPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showMergeRequest(mrInfo: MergeRequestInfoView.MergeRequestInfo) {
        toolbar.title = mrInfo.mr.id.toString()
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
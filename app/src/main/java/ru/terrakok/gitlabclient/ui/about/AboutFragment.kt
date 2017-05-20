package ru.terrakok.gitlabclient.ui.about

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_about.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.about.AboutPresenter
import ru.terrakok.gitlabclient.presentation.about.AboutView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
class AboutFragment : BaseFragment(), AboutView {
    override val layoutRes = R.layout.fragment_about

    @InjectPresenter lateinit var presenter: AboutPresenter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }
    }

    override fun showAppVersion(version: String) {
        aboutVersionTV.text = version
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
package ru.terrakok.gitlabclient.ui.about

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_about.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.extension.tryOpenLink
import ru.terrakok.gitlabclient.presentation.about.AboutPresenter
import ru.terrakok.gitlabclient.presentation.about.AboutView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
class AboutFragment : BaseFragment(), AboutView {
    override val layoutRes = R.layout.fragment_about

    @InjectPresenter
    lateinit var presenter: AboutPresenter

    private var supportUrl: String? = null

    @ProvidePresenter
    fun providePresenter(): AboutPresenter =
        scope.getInstance(AboutPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }
        feedbackView.setOnClickListener { tryOpenLink(supportUrl) }
        librariesView.setOnClickListener { presenter.onShowLibrariesClicked() }
        privacyPolicyView.setOnClickListener { presenter.onPrivacyPolicyClicked() }
        authorsView.setOnClickListener { presenter.onDevelopersClicked() }
    }

    override fun showAppInfo(appInfo: AppInfo) {
        supportUrl = appInfo.feedbackUrl
        versionTextView.text = "${appInfo.versionName} (${appInfo.versionCode} ${appInfo.buildId})"
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
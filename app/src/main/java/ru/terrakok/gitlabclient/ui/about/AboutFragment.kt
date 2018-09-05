package ru.terrakok.gitlabclient.ui.about

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.item_app_developer.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.AppDeveloper
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.extension.loadRoundedImage
import ru.terrakok.gitlabclient.extension.sendEmail
import ru.terrakok.gitlabclient.extension.tryOpenLink
import ru.terrakok.gitlabclient.presentation.about.AboutPresenter
import ru.terrakok.gitlabclient.presentation.about.AboutView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
class AboutFragment : BaseFragment(), AboutView {
    override val layoutRes = R.layout.fragment_about

    @InjectPresenter
    lateinit var presenter: AboutPresenter

    private var supportUrl: String? = null

    @ProvidePresenter
    fun providePresenter(): AboutPresenter {
        return Toothpick
            .openScope(DI.DRAWER_FLOW_SCOPE)
            .getInstance(AboutPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }
        feedbackView.setOnClickListener { tryOpenLink(supportUrl) }
        librariesView.setOnClickListener { presenter.onShowLibrariesClicked() }
    }

    override fun showAppInfo(appInfo: AppInfo) {
        supportUrl = appInfo.feedbackUrl
        versionTextView.text = "${appInfo.versionName} (${appInfo.versionCode} ${appInfo.buildId})"
    }

    override fun showAppDevelopers(devs: List<AppDeveloper>) {
        developersContainer.removeAllViews()
        devs.forEach { developer ->
            developersContainer.inflate(R.layout.item_app_developer, false).apply {
                this.nameTextView.text = developer.name
                this.roleTextView.text = developer.role
                this.setOnClickListener {
                    if (developer.gitlabId != null)
                        presenter.onDeveloperClicked(developer.gitlabId)
                    else
                        sendEmail(developer.email)
                }
                this.avatarImageView.loadRoundedImage(developer.avatarUrl, this@AboutFragment.context)

                developersContainer.addView(this)
            }
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
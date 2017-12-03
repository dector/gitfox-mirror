package ru.terrakok.gitlabclient.ui.about

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_about.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.AppDeveloper
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
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

    @InjectPresenter lateinit var presenter: AboutPresenter

    @ProvidePresenter
    fun providePresenter(): AboutPresenter {
        return Toothpick
                .openScope(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(AboutPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }
    }

    override fun showAppInfo(appInfo: AppInfo) {
        versionNameTextView.text = appInfo.versionName
        versionCodeTextView.text = "(${appInfo.versionCode} ${appInfo.buildId})"
    }

    override fun showAppDevelopers(devs: List<AppDeveloper>) {

    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
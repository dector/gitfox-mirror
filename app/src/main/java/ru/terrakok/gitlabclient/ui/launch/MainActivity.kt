package ru.terrakok.gitlabclient.ui.launch

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.mvp.launch.LaunchPresenter
import ru.terrakok.gitlabclient.mvp.launch.LaunchView
import ru.terrakok.gitlabclient.ui.auth.AuthFragment
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.main.MainFragment
import javax.inject.Inject

class MainActivity : BaseActivity(), LaunchView {
    @Inject
    lateinit var navigationHolder: NavigatorHolder

    @InjectPresenter
    lateinit var presenter: LaunchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.DAGGER.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    private val navigator = object : SupportAppNavigator(this, R.id.mainContainer) {

        override fun createActivityIntent(screenKey: String?, data: Any?) = null

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            Screens.MAIN_SCREEN -> MainFragment()
            else -> null
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.mainContainer)
        if (fragment is BaseFragment) {
            fragment.onBackPressed()
        } else {
            presenter.onBackPressed()
        }
    }
}

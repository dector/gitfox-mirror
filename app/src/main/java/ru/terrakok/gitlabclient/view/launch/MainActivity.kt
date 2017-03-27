package ru.terrakok.gitlabclient.view.launch

import android.os.Bundle
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.presentation.launch.LaunchPresenter
import ru.terrakok.gitlabclient.presentation.launch.LaunchView
import ru.terrakok.gitlabclient.view.auth.AuthFragment
import ru.terrakok.gitlabclient.view.global.BaseActivity
import ru.terrakok.gitlabclient.view.global.BaseFragment
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

    private val navigator = object : SupportFragmentNavigator(supportFragmentManager, R.id.mainContainer) {

        override fun createFragment(screenKey: String?, data: Any?) = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            else -> null
        }

        override fun exit() = finish()

        override fun showSystemMessage(message: String?)
                = Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
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

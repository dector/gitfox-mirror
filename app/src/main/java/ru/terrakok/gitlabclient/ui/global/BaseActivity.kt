package ru.terrakok.gitlabclient.ui.global

import android.content.Context
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseActivity : MvpAppCompatActivity() {
    @Inject lateinit var navigatorHolder: NavigatorHolder

    protected abstract val layoutRes: Int
    protected abstract val navigator: Navigator

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
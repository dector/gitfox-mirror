package ru.terrakok.gitlabclient.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.arellomobile.mvp.MvpAppCompatActivity
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_container.*
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.model.system.message.SystemMessageNotifier
import ru.terrakok.gitlabclient.model.system.message.SystemMessageType
import ru.terrakok.gitlabclient.presentation.AppLauncher
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.MessageDialogFragment
import ru.terrakok.gitlabclient.util.doOnApplyWindowInsets
import ru.terrakok.gitlabclient.util.updatePadding
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */

class AppActivity : MvpAppCompatActivity() {

    @Inject
    lateinit var appLauncher: AppLauncher

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var systemMessageNotifier: SystemMessageNotifier

    private var notifierDisposable: Disposable? = null

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    private val navigator: Navigator =
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {
            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                // Fix incorrect order lifecycle callback of MainFragment
                fragmentTransaction.setReorderingAllowed(true)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decorView.systemUiVisibility = decorView.systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }

                statusBarColor = ContextCompat.getColor(context, R.color.black_20)
                navigationBarColor = ContextCompat.getColor(context, R.color.white_50)
            }
        }

        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        appLauncher.onLaunch()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)

        if (savedInstanceState == null) {
            appLauncher.coldStart()
        }
        container.doOnApplyWindowInsets { view, insets, initialPadding ->
            view.updatePadding(
                left = initialPadding.left + insets.systemWindowInsetLeft,
                right = initialPadding.right + insets.systemWindowInsetRight
            )
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        subscribeOnSystemMessages()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        unsubscribeOnSystemMessages()
        super.onPause()
    }

    override fun onBackPressed() {
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }

    private fun showAlertMessage(message: String) {
        MessageDialogFragment.create(
            message = message
        ).show(supportFragmentManager, null)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeOnSystemMessages() {
        notifierDisposable = systemMessageNotifier.notifier
            .subscribe { msg ->
                when (msg.type) {
                    SystemMessageType.ALERT -> showAlertMessage(msg.text)
                    SystemMessageType.TOAST -> showToastMessage(msg.text)
                }
            }
    }

    private fun unsubscribeOnSystemMessages() {
        notifierDisposable?.dispose()
    }
}
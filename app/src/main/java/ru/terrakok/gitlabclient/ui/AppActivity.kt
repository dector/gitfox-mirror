package ru.terrakok.gitlabclient.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.message.SystemMessageNotifier
import ru.terrakok.gitlabclient.model.system.message.SystemMessageType
import ru.terrakok.gitlabclient.presentation.AppPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.MessageDialogFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class AppActivity : MvpAppCompatActivity(), MvpView {

    @InjectPresenter
    lateinit var presenter: AppPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.SERVER_SCOPE)
            .getInstance(AppPresenter::class.java)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var systemMessageNotifier: SystemMessageNotifier

    private var notifierDisposable: Disposable? = null

    private val currentFragment: BaseFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFragment

    private val navigator: Navigator =
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {
            override fun createActivityIntent(
                context: Context,
                screenKey: String,
                data: Any?
            ): Intent? = Screens.createIntent(screenKey, data)

            override fun exit() {
                finish()
            }

            override fun createFragment(screenKey: String, data: Any?) =
                Screens.createFragment(screenKey, data)

            override fun setupFragmentTransactionAnimation(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                //fix incorrect order lifecycle callback of MainFragment
                fragmentTransaction.setReorderingAllowed(true)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        Toothpick.inject(this, Toothpick.openScope(DI.SERVER_SCOPE))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_container)
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
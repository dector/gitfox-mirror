package ru.terrakok.gitlabclient.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowNavigator
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.UserId
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import ru.terrakok.gitlabclient.ui.user.info.UserInfoFragment
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserActivity : BaseActivity() {
    @Inject lateinit var navigationHolder: NavigatorHolder

    override val layoutRes = R.layout.activity_container
    private val userId get() = intent.getLongExtra(ARG_USER_ID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        Toothpick.openScopes(DI.SERVER_SCOPE, DI.USER_SCOPE).apply {
            installModules(object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(UserId::class.java)
                            .toInstance(PrimitiveWrapper(userId))
                }
            })
        }

        if (savedInstanceState == null) {
            navigator.setLaunchScreen(Screens.USER_INFO_SCREEN, null)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) Toothpick.closeScope(DI.USER_SCOPE)
    }

    private val navigator = object : FlowNavigator(this, R.id.container) {

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.USER_INFO_SCREEN -> UserInfoFragment()
            else -> null
        }
    }

    companion object {
        private val ARG_USER_ID = "arg_user_id"
        fun getStartIntent(userId: Long, context: Context) =
                Intent(context, UserActivity::class.java).apply {
                    putExtra(ARG_USER_ID, userId)
                }
    }
}
package ru.terrakok.gitlabclient.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowNavigator
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 29.10.17.
 */
class AuthActivity : BaseActivity() {
    override val layoutRes = R.layout.activity_container

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigator.setLaunchScreen(Screens.AUTH_SCREEN, null)
        }
    }

    override val navigator = object : FlowNavigator(this, R.id.container) {

        override fun createFlowIntent(flowKey: String, data: Any?) =
                Screens.getFlowIntent(this@AuthActivity, flowKey, data)

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.AUTH_SCREEN -> AuthFragment()
            else -> null
        }
    }

    companion object {
        fun getStartIntent(context: Context) =
                Intent(context, AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
    }
}
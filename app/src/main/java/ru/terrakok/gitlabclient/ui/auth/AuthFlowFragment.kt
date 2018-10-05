package ru.terrakok.gitlabclient.ui.auth

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.setLaunchScreen
import ru.terrakok.gitlabclient.presentation.auth.AuthFlowPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.FlowNavigationModule
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 29.10.17.
 */
class AuthFlowFragment : FlowFragment(), MvpView {

    @InjectPresenter
    lateinit var presenter: AuthFlowPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.AUTH_FLOW_SCOPE)
            .getInstance(AuthFlowPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        initScope()
        super.onCreate(savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.Auth)
        }
    }

    private fun initScope() {
        val scope = Toothpick.openScopes(DI.SERVER_SCOPE, DI.AUTH_FLOW_SCOPE)
        scope.installModules(
            FlowNavigationModule(scope.getInstance(Router::class.java))
        )
        Toothpick.inject(this, scope)
    }

    override fun onExit() {
        presenter.onExit()
    }
}
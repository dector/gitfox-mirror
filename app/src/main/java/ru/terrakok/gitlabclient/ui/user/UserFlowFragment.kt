package ru.terrakok.gitlabclient.ui.user

import android.os.Bundle
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.setLaunchScreen
import ru.terrakok.gitlabclient.presentation.user.UserFlowPresenter
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.module.FlowNavigationModule
import ru.terrakok.gitlabclient.toothpick.qualifier.UserId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Toothpick
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserFlowFragment : FlowFragment(), MvpView {

    private val userId by argument(ARG_USER_ID, 0L)

    @InjectPresenter
    lateinit var presenter: UserFlowPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.USER_FLOW_SCOPE)
            .getInstance(UserFlowPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        prepareScope(savedInstanceState == null)
        super.onCreate(savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setLaunchScreen(Screens.UserInfo)
        }
    }

    private fun prepareScope(firstTime: Boolean) {
        val scope = Toothpick.openScopes(DI.SERVER_SCOPE, DI.USER_FLOW_SCOPE)
        if (firstTime) {
            scope.installModules(
                FlowNavigationModule(scope.getInstance(Router::class.java)),
                object : Module() {
                    init {
                        bind(PrimitiveWrapper::class.java)
                            .withName(UserId::class.java)
                            .toInstance(PrimitiveWrapper(userId))
                    }
                }
            )
        }
        Toothpick.inject(this, scope)
    }

    override fun onExit() {
        presenter.onExit()
    }

    companion object {
        private const val ARG_USER_ID = "arg_user_id"
        fun create(userId: Long) =
            UserFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_USER_ID, userId)
                }
            }
    }
}
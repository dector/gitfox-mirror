package ru.terrakok.gitlabclient.ui.user

import android.os.Bundle
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.UserId
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Scope
import toothpick.config.Module

class UserFlowFragment : FlowFragment() {

    private val userId by argument(ARG_USER_ID, 0L)

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(UserId::class.java)
                        .toInstance(PrimitiveWrapper(userId))
                }
            }
        )
    }

    override fun getLaunchScreen() = Screens.UserInfo

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
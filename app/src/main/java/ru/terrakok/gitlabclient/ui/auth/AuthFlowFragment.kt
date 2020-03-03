package ru.terrakok.gitlabclient.ui.auth

import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.ui.global.FlowFragment

class AuthFlowFragment : FlowFragment() {
    override fun getLaunchScreen() = Screens.Auth
}

package ru.terrakok.gitlabclient.system

import com.github.aakira.napier.Napier
import gitfox.entity.app.session.UserAccount
import gitfox.model.data.state.SessionSwitcher
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.module.ServerModule
import toothpick.Toothpick

class SessionSwitcherImpl : SessionSwitcher {
    override val hasSession: Boolean get() = Toothpick.isScopeOpen(DI.SERVER_SCOPE)

    override fun initSession(newAccount: UserAccount?) {
        Toothpick.closeScope(DI.SERVER_SCOPE)
        Napier.d("Init new scope: ${DI.SERVER_SCOPE} -> ${DI.APP_SCOPE}")
        Toothpick
            .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
            .installModules(ServerModule(newAccount))
    }
}
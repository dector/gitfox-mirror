package ru.terrakok.gitlabclient.model.data.state

import com.github.aakira.napier.Napier
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.module.ServerModule
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import toothpick.Toothpick

class SessionSwitcher {
    val hasSession: Boolean get() = Toothpick.isScopeOpen(DI.SERVER_SCOPE)

    fun initSession(newAccount: UserAccount?) {
        Toothpick.closeScope(DI.SERVER_SCOPE)
        Napier.d("Init new scope: ${DI.SERVER_SCOPE} -> ${DI.APP_SCOPE}")
        Toothpick
                .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
                .installModules(ServerModule(newAccount))
    }
}
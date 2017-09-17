package ru.terrakok.gitlabclient.model.system

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.ServerModule
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import toothpick.Toothpick
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 17.09.17.
 */
class ServerSwitcher @Inject constructor(
        private val authHolder: AuthHolder,
        @ServerPath private val url: String
) {
    private val restartRelay = PublishRelay.create<Boolean>()
    val restartLaunchActivitySignal get() = restartRelay as Observable<Boolean>

    fun switchOnNewServer() {
        if (url != authHolder.serverPath) {
            Toothpick.closeScope(DI.SERVER_SCOPE)
            Toothpick
                    .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
                    .installModules(ServerModule(authHolder.serverPath))

        }
        restartRelay.accept(true)
    }
}
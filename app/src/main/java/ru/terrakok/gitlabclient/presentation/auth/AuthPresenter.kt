package ru.terrakok.gitlabclient.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.model.server.ServerConfig
import java.util.*
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@InjectViewState
class AuthPresenter : MvpPresenter<AuthView>() {
    @Inject
    lateinit var router: Router
    @Inject
    lateinit var serverConfig: ServerConfig

    private val authHash = UUID.randomUUID().toString()

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.loadUrl(serverConfig.getAuthUrl(authHash))
    }

    fun onBackPressed() = router.exit()

}
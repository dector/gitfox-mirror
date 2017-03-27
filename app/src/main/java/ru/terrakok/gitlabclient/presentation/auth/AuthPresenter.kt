package ru.terrakok.gitlabclient.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.model.resources.ResourceManager
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
    @Inject
    lateinit var resourceManager: ResourceManager

    private val authHash = UUID.randomUUID().toString()

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startAuthorization()
    }

    private fun startAuthorization() {
        viewState.loadUrl(serverConfig.getAuthUrl(authHash))
    }

    private fun requestToken(code: String) {

    }

    fun onRedirect(url: String): Boolean {
        if (serverConfig.checkRedirect(url)) {
            if (!url.contains(authHash)) {
                viewState.showMessage(resourceManager.getString(R.string.invalid_hash))
                startAuthorization()
            } else {
                requestToken(serverConfig.getCodeFromAuthRedirect(url))
            }
            return true
        } else {
            viewState.loadUrl(url)
            return false
        }
    }

    fun onBackPressed() = router.exit()

}
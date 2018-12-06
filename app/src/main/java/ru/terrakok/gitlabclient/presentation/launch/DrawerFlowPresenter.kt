package ru.terrakok.gitlabclient.presentation.launch

import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class DrawerFlowPresenter @Inject constructor(
    private val router: Router,
    private val scope: Scope
) : BasePresenter<MvpView>() {

    override fun onDestroy() {
        Toothpick.closeScope(scope.name)
        super.onDestroy()
    }

    fun onExit() {
        router.exit()
    }
}
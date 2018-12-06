package ru.terrakok.gitlabclient.presentation.user

import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import toothpick.Scope
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class UserFlowPresenter @Inject constructor(
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
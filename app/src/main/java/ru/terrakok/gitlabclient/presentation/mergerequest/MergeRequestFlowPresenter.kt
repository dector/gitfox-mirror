package ru.terrakok.gitlabclient.presentation.mergerequest

import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.toothpick.DI
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class MergeRequestFlowPresenter @Inject constructor(
    private val router: Router
) : BasePresenter<MvpView>() {

    override fun onDestroy() {
        Toothpick.closeScope(DI.MERGE_REQUEST_FLOW_SCOPE)
        super.onDestroy()
    }

    fun onExit() {
        router.exit()
    }
}
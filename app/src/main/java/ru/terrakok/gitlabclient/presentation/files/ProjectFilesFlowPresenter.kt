package ru.terrakok.gitlabclient.presentation.files

import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.toothpick.DI
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 04.11.18.
 */
class ProjectFilesFlowPresenter @Inject constructor(
    private val router: Router
) : BasePresenter<MvpView>() {

    override fun onDestroy() {
        Toothpick.closeScope(DI.PROJECT_FILES_FLOW_SCOPE)
        super.onDestroy()
    }

    fun onExit() = router.exit()
}
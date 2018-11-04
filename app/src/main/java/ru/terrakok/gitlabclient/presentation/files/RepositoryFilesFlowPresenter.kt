package ru.terrakok.gitlabclient.presentation.files

import com.arellomobile.mvp.MvpView
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.toothpick.DI
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 04.11.18.
 */
class RepositoryFilesFlowPresenter @Inject constructor() : BasePresenter<MvpView>() {

    override fun onDestroy() {
        Toothpick.closeScope(DI.REPOSITORY_FILES_FLOW_SCOPE)
        super.onDestroy()
    }
}
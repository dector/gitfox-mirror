package ru.terrakok.gitlabclient.ui.project

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectName
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
@InjectViewState
class ProjectPresenter @Inject constructor(
        @ProjectName private val projectNameWrapper: PrimitiveWrapper<String>,
        private val router: Router
) : BasePresenter<ProjectView>() {

    private val projectName = projectNameWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setProjectName(projectName)
    }

    fun onBackPressed() = router.exit()
}
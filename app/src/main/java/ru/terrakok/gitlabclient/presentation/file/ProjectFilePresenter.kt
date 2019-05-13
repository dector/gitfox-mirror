package ru.terrakok.gitlabclient.presentation.file

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.extension.extractFileNameFromPath
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.FilePath
import ru.terrakok.gitlabclient.toothpick.qualifier.FileReference
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
@InjectViewState
class ProjectFilePresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @FilePath private val filePath: String,
    @FileReference private val fileReference: String,
    private val projectInteractor: ProjectInteractor,
    private val errorHandler: ErrorHandler,
    private val router: Router
) : BasePresenter<ProjectFileView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setTitle(filePath.extractFileNameFromPath())
        // Progress will be hidden, after code will be highlighted.
        viewState.showEmptyProgress(true)

        projectInteractor.getProjectRawFile(projectId, filePath, fileReference)
            .subscribe(
                { viewState.setRawFile(it) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    fun onCodeHighlightStarted() {
        viewState.showEmptyProgress(true)
    }

    fun onCodeHighlightSFinished() {
        viewState.showEmptyProgress(false)
    }

    fun onBackPressed() = router.exit()
}
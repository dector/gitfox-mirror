package ru.terrakok.gitlabclient.presentation.file

import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.di.FilePath
import ru.terrakok.gitlabclient.di.FileReference
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.util.extractFileNameFromPath
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
    private val router: Router
) : BasePresenter<ProjectFileView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.setTitle(filePath.extractFileNameFromPath())
        loadProjectFileRaw(false)
    }

    fun onFileReload() = loadProjectFileRaw(true)

    private fun loadProjectFileRaw(isReload: Boolean) {
        launch {
            if (isReload) viewState.showEmptyView(false)
            try {
                val f = projectInteractor.getProjectRawFile(projectId, filePath, fileReference)
                viewState.setRawFile(f)
            } catch (e: Exception) {
                viewState.showEmptyView(true)
            }
        }
    }

    fun onBackPressed() = router.exit()
}

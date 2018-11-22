package ru.terrakok.gitlabclient.presentation.file

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.BranchName
import ru.terrakok.gitlabclient.toothpick.qualifier.FileName
import ru.terrakok.gitlabclient.toothpick.qualifier.FilePath
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
@InjectViewState
class ProjectFilePresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @FileName fileName: String,
    @FilePath filePath: String,
    @BranchName branchName: String,
    private val projectInteractor: ProjectInteractor,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter
) : BasePresenter<ProjectFileView>() {

    private val projectId = projectIdWrapper.value

    fun onBackPressed() = flowRouter.exit()
}
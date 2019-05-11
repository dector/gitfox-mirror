package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Scope
import toothpick.config.Module

class MergeRequestFlowFragment : FlowFragment() {

    private val mrId by argument(ARG_MR_ID, 0L)
    private val projectId by argument(ARG_PROJECT_ID, 0L)

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                    bind(PrimitiveWrapper::class.java)
                        .withName(MergeRequestId::class.java)
                        .toInstance(PrimitiveWrapper(mrId))
                }
            }
        )
    }

    override fun getLaunchScreen() = Screens.MainMergeRequest

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_MR_ID = "arg_mr_id"
        fun create(projectId: Long, mrId: Long) =
            MergeRequestFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putLong(ARG_MR_ID, mrId)
                }
            }
    }
}
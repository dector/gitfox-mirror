package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import ru.terrakok.gitlabclient.util.argument
import toothpick.Scope
import toothpick.config.Module

class ProjectFlowFragment : FlowFragment() {

    private val projectId by argument(ARG_PROJECT_ID, 0L)

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                }
            }
        )
    }

    override fun getLaunchScreen() = Screens.MainProject

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        fun create(projectId: Long) =
            ProjectFlowFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                }
            }
    }
}
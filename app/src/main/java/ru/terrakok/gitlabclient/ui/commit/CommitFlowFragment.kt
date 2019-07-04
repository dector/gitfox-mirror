package ru.terrakok.gitlabclient.ui.commit

import android.os.Bundle
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.CommitId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.ui.global.FlowFragment
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitFlowFragment : FlowFragment() {

    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val commitId by argument(ARG_COMMIT_ID, "")

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                    bind(String::class.java)
                            .withName(CommitId::class.java)
                            .toInstance(commitId)
                }
            }
        )
    }

    override fun getLaunchScreen() = Screens.Commit

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_COMMIT_ID = "arg_commit_id"

        fun create(commitId: String, projectId: Long) =
                CommitFlowFragment().apply {
                    arguments = Bundle().apply {
                        putLong(ARG_PROJECT_ID, projectId)
                        putString(ARG_COMMIT_ID, commitId)
                    }
                }
    }
}
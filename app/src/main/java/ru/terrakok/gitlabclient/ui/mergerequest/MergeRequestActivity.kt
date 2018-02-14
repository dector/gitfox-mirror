package ru.terrakok.gitlabclient.ui.mergerequest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowNavigator
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.MergeRequestId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class MergeRequestActivity : BaseActivity() {
    @Inject lateinit var navigationHolder: NavigatorHolder

    override val layoutRes = R.layout.activity_container
    private val mrId get() = intent.getLongExtra(ARG_MR_ID, 0)
    private val projectId get() = intent.getLongExtra(ARG_PROJECT_ID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        Toothpick.openScopes(DI.SERVER_SCOPE, DI.MERGE_REQUEST_SCOPE).apply {
            installModules(object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(MergeRequestId::class.java)
                            .toInstance(PrimitiveWrapper(mrId))
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                }
            })
        }

        if (savedInstanceState == null) {
            navigator.setLaunchScreen(Screens.MR_SCREEN, null)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigationHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) Toothpick.closeScope(DI.MERGE_REQUEST_SCOPE)
    }

    private val navigator = object : FlowNavigator(this, R.id.container) {

        override fun createFragment(screenKey: String?, data: Any?) = when(screenKey) {
            Screens.MR_SCREEN -> MergeRequestFragment()
            else -> null
        }
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_MR_ID = "arg_mr_id"
        fun getStartIntent(projectId: Long, mrId: Long, context: Context) =
                Intent(context, MergeRequestActivity::class.java).apply {
                    putExtra(ARG_PROJECT_ID, projectId)
                    putExtra(ARG_MR_ID, mrId)
                }
    }
}
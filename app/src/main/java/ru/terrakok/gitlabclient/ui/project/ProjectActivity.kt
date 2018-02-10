package ru.terrakok.gitlabclient.ui.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowNavigator
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectName
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import toothpick.Toothpick
import toothpick.config.Module
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class ProjectActivity : BaseActivity() {
    @Inject lateinit var navigationHolder: NavigatorHolder

    override val layoutRes = R.layout.activity_container
    private val projectId get() = intent.getLongExtra(ARG_PROJECT_ID, 0)
    private val projectName get() = intent.getStringExtra(ARG_PROJECT_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        Toothpick.openScopes(DI.SERVER_SCOPE, DI.PROJECT_SCOPE).apply {
            installModules(object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectName::class.java)
                            .toInstance(PrimitiveWrapper(projectName))
                }
            })
        }

        if (savedInstanceState == null) {
            navigator.setLaunchScreen(Screens.PROJECT_SCREEN, null)
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

        if (isFinishing) Toothpick.closeScope(DI.PROJECT_SCOPE)
    }

    private val navigator = object : FlowNavigator(this, R.id.container) {

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.PROJECT_SCREEN -> ProjectFragment()
            else -> null
        }
    }

    companion object {
        private val ARG_PROJECT_ID = "arg_project_id"
        private val ARG_PROJECT_NAME = "arg_project_name"
        fun getStartIntent(projectId: Long, projectName: String, context: Context) =
                Intent(context, ProjectActivity::class.java).apply {
                    putExtra(ARG_PROJECT_ID, projectId)
                    putExtra(ARG_PROJECT_NAME, projectName)
                }
    }
}
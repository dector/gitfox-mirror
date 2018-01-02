package ru.terrakok.gitlabclient.ui.project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Replace
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.BaseActivity
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)

        Toothpick.openScopes(DI.SERVER_SCOPE, DI.PROJECT_SCOPE).apply {
            installModules(object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                            .withName(ProjectId::class.java)
                            .toInstance(PrimitiveWrapper(projectId))
                }
            })
        }

        if (savedInstanceState == null) {
            navigator.applyCommand(Replace(Screens.PROJECT_INFO_SCREEN, projectId))
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

    private val navigator = object : SupportAppNavigator(this, R.id.container) {

        override fun createActivityIntent(screenKey: String?, data: Any?) = null

        override fun createFragment(screenKey: String?, data: Any?): Fragment? = when (screenKey) {
            Screens.PROJECT_INFO_SCREEN -> ProjectInfoFragment()
            else -> null
        }
    }

    companion object {
        private val ARG_PROJECT_ID = "arg_project_id"
        fun getStartIntent(projectId: Long, context: Context) =
                Intent(context, ProjectActivity::class.java).apply {
                    putExtra(ARG_PROJECT_ID, projectId)
                }
    }
}
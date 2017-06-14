package ru.terrakok.gitlabclient.dagger.component

import dagger.Component
import ru.terrakok.gitlabclient.dagger.module.*
import ru.terrakok.gitlabclient.presentation.about.AboutPresenter
import ru.terrakok.gitlabclient.presentation.auth.AuthPresenter
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerPresenter
import ru.terrakok.gitlabclient.presentation.launch.LaunchPresenter
import ru.terrakok.gitlabclient.presentation.main.MainPresenter
import ru.terrakok.gitlabclient.presentation.my.issues.MyIssuesPresenter
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoPresenter
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.ui.launch.MainActivity
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
@Component(modules = arrayOf(
        AppModule::class,
        NavigationModule::class,
        AuthModule::class,
        ProfileModule::class,
        ProjectModule::class,
        IssueModule::class
))
@Singleton
interface AppComponent {
    fun inject(presenter: LaunchPresenter)
    fun inject(presenter: AuthPresenter)
    fun inject(presenter: ProjectsListPresenter)
    fun inject(activity: MainActivity)
    fun inject(presenter: MainPresenter)
    fun inject(presenter: NavigationDrawerPresenter)
    fun inject(presenter: ProjectInfoPresenter)
    fun inject(presenter: AboutPresenter)
    fun inject(presenter: MyIssuesPresenter)
}
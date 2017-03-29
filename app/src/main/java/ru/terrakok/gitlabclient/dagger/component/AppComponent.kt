package ru.terrakok.gitlabclient.dagger.component

import dagger.Component
import ru.terrakok.gitlabclient.dagger.module.AppModule
import ru.terrakok.gitlabclient.dagger.module.NavigationModule
import ru.terrakok.gitlabclient.mvp.auth.AuthPresenter
import ru.terrakok.gitlabclient.mvp.launch.LaunchPresenter
import ru.terrakok.gitlabclient.mvp.main.ProjectsListPresenter
import ru.terrakok.gitlabclient.ui.launch.MainActivity
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
@Component(modules = arrayOf(
        AppModule::class,
        NavigationModule::class
))
@Singleton
interface AppComponent {
    fun inject(presenter: LaunchPresenter)
    fun inject(presenter: AuthPresenter)
    fun inject(presenter: ProjectsListPresenter)
    fun inject(activity: MainActivity)
}
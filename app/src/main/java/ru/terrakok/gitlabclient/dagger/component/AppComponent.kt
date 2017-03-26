package ru.terrakok.gitlabclient.dagger.component

import dagger.Component
import ru.terrakok.gitlabclient.dagger.module.AppModule
import ru.terrakok.gitlabclient.dagger.module.NavigationModule
import ru.terrakok.gitlabclient.presentation.launch.LaunchPresenter
import ru.terrakok.gitlabclient.view.launch.MainActivity
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
    fun inject(activity: MainActivity)
}
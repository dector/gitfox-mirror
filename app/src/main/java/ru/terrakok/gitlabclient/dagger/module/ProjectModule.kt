package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.interactor.project.Base64Tools
import ru.terrakok.gitlabclient.model.interactor.project.MarkDownConverter
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.interactor.projects.MainProjectsListInteractor
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */

@Module
class ProjectModule {

    @Provides
    fun provideProjectRepository(api: GitlabApi, schedulers: SchedulersProvider) =
            ProjectRepository(api, schedulers)

    @Provides
    fun provideMainProjectsListInteractor(projectRepository: ProjectRepository)
            = MainProjectsListInteractor(projectRepository)

    @Provides
    fun provideProjectInteractor(projectRepository: ProjectRepository, schedulers: SchedulersProvider)
            = ProjectInteractor(projectRepository, MarkDownConverter(), schedulers, Base64Tools())
}
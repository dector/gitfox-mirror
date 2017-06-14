package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.interactor.issue.MyIssuesInteractor
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */

@Module
class IssueModule {

    @Provides
    fun provideIssueRepository(api: GitlabApi, schedulers: SchedulersProvider) =
            IssueRepository(api, schedulers)

    @Provides
    fun provideMyIssuesInteractor(issueRepository: IssueRepository)
            = MyIssuesInteractor(issueRepository)
}
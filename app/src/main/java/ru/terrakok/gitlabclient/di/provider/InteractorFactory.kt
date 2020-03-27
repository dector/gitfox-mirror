package ru.terrakok.gitlabclient.di.provider

import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.data.state.SessionSwitcher
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.data.storage.RawAppData
import ru.terrakok.gitlabclient.model.interactor.*
import javax.inject.Inject
import javax.inject.Provider

class AccountInteractorProvider @Inject constructor(
    @ServerPath private val serverPath: String,
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    private val todoInteractor: TodoInteractor,
    private val mrInteractor: MergeRequestInteractor,
    private val issueInteractor: IssueInteractor
) : Provider<AccountInteractor> {
    override fun get() = AccountInteractor(serverPath, api, serverChanges, todoInteractor, mrInteractor, issueInteractor)
}

class AppInfoInteractorProvider @Inject constructor(
    private val rawAppData: RawAppData,
    private val appInfo: AppInfo
) : Provider<AppInfoInteractor> {
    override fun get() = AppInfoInteractor(rawAppData, appInfo)
}

class CommitInteractorProvider @Inject constructor(
    @ServerPath private val serverPath: String,
    private val api: GitlabApi
) : Provider<CommitInteractor> {
    override fun get() = CommitInteractor(api)
}

class EventInteractorProvider @Inject constructor(
    private val api: GitlabApi,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<EventInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = EventInteractor(api, defaultPageSize)
}

class IssueInteractorProvider @Inject constructor(
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<IssueInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = IssueInteractor(api, serverChanges, defaultPageSize)
}

class LabelInteractorProvider @Inject constructor(
    @ServerPath private val serverPath: String,
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<LabelInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = LabelInteractor(api, serverChanges, defaultPageSize)
}

class LaunchInteractorProvider @Inject constructor(
    private val prefs: Prefs,
    private val sessionSwitcher: SessionSwitcher
) : Provider<LaunchInteractor> {
    override fun get() = LaunchInteractor(prefs, sessionSwitcher)
}

class MembersInteractorProvider @Inject constructor(
    @ServerPath private val serverPath: String,
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<MembersInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = MembersInteractor(api, serverChanges, defaultPageSize)
}

class MergeRequestInteractorProvider @Inject constructor(
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<MergeRequestInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = MergeRequestInteractor(api, serverChanges, defaultPageSize)
}

class MilestoneInteractorProvider @Inject constructor(
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<MilestoneInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = MilestoneInteractor(api, serverChanges, defaultPageSize)
}

class ProjectInteractorProvider @Inject constructor(
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<ProjectInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = ProjectInteractor(api, serverChanges, defaultPageSize)
}

class SessionInteractorProvider @Inject constructor(
    private val prefs: Prefs,
    private val oauthParams: OAuthParams,
    private val userAccountApi: UserAccountApi,
    private val projectCache: ProjectCache,
    private val sessionSwitcher: SessionSwitcher
) : Provider<SessionInteractor> {
    override fun get() = SessionInteractor(prefs, oauthParams, userAccountApi, projectCache, sessionSwitcher)
}

class TodoInteractorProvider @Inject constructor(
    private val api: GitlabApi,
    private val serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) : Provider<TodoInteractor> {
    private val defaultPageSize = defaultPageSizeWrapper.value
    override fun get() = TodoInteractor(api, serverChanges, defaultPageSize)
}

class UserInteractorProvider @Inject constructor(
    private val api: GitlabApi
) : Provider<UserInteractor> {
    override fun get() = UserInteractor(api)
}

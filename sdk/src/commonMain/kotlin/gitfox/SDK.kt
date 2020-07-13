package gitfox

import com.russhwolf.settings.Settings
import gitfox.entity.app.session.AuthHolder
import gitfox.entity.app.session.OAuthParams
import gitfox.entity.app.session.UserAccount
import gitfox.model.data.cache.ProjectCache
import gitfox.model.data.cache.ProjectLabelCache
import gitfox.model.data.cache.ProjectMilestoneCache
import gitfox.model.data.server.*
import gitfox.model.data.state.ServerChanges
import gitfox.model.data.state.SessionSwitcher
import gitfox.model.data.storage.Prefs
import gitfox.model.interactor.*
import gitfox.util.HttpClientFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

open class SDK internal constructor(
    private val defaultPageSize: Int = SDK.defaultPageSize,
    private val cacheLifetime: Long = SDK.cacheLifetime,
    private val oAuthParams: OAuthParams,
    private val isDebug: Boolean,
    private val httpClientFactory: HttpClientFactory,
    private val settings: Settings
) {

    private val json = Json(JsonConfiguration.Stable.copy(
        ignoreUnknownKeys = true,
        isLenient = true,
        encodeDefaults = false
    ))

    private val userAccountApi = UserAccountApi(httpClientFactory.create(json, null, isDebug))
    private val serverChanges = ServerChanges()
    private val prefs = Prefs(settings, json)

    private val projectLabelCache = ProjectLabelCache(cacheLifetime)
    private val projectMilestoneCache = ProjectMilestoneCache(cacheLifetime)

    private data class Session(
        val account: UserAccount?,
        val api: GitlabApi
    )

    private var currentSession = createNewSession(null)

    private val sessionSwitcher = object : SessionSwitcher {
        override val hasSession = currentSession.account != null
        override fun initSession(newAccount: UserAccount?) {
            currentSession = createNewSession(newAccount)
        }
    }

    fun getCurrentServerPath() = currentSession.api.endpoint
    fun getLaunchInteractor() = LaunchInteractor(prefs, sessionSwitcher)
    fun getCommitInteractor() = CommitInteractor(currentSession.api)
    fun getEventInteractor() = EventInteractor(currentSession.api, defaultPageSize)
    fun getIssueInteractor() = IssueInteractor(currentSession.api, serverChanges, defaultPageSize)
    fun getLabelInteractor() = LabelInteractor(currentSession.api, serverChanges, defaultPageSize, projectLabelCache)
    fun getMembersInteractor() = MembersInteractor(currentSession.api, serverChanges, defaultPageSize)
    fun getMergeRequestInteractor() = MergeRequestInteractor(currentSession.api, serverChanges, defaultPageSize)
    fun getMilestoneInteractor() = MilestoneInteractor(currentSession.api, serverChanges, defaultPageSize, projectMilestoneCache)
    fun getProjectInteractor() = ProjectInteractor(currentSession.api, serverChanges, defaultPageSize)
    fun getTodoInteractor() = TodoInteractor(currentSession.api, serverChanges, defaultPageSize)
    fun getUserInteractor() = UserInteractor(currentSession.api)

    fun getSessionInteractor() = SessionInteractor(
        prefs,
        oAuthParams,
        userAccountApi,
        sessionSwitcher
    )

    fun getAccountInteractor() = AccountInteractor(
        currentSession.api.endpoint,
        currentSession.api,
        serverChanges,
        getTodoInteractor(),
        getMergeRequestInteractor(),
        getIssueInteractor()
    )

    private fun createNewSession(account: UserAccount?) = Session(
        account,
        ApiWithChangesRegistration(
            ApiWithProjectCache(
                GitlabApiImpl(
                    account?.serverPath ?: oAuthParams.endpoint,
                    httpClientFactory.create(
                        json,
                        AuthHolder(account?.token, account?.isOAuth ?: true),
                        isDebug
                    )
                ),
                ProjectCache(cacheLifetime)
            ),
            serverChanges
        )
    )

    companion object {
        const val defaultPageSize: Int = 20
        const val cacheLifetime: Long = 300_000L
    }
}

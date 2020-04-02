package gitfox

import com.russhwolf.settings.AppleSettings
import gitfox.client.HttpClientFactory
import gitfox.entity.app.develop.AppInfo
import gitfox.entity.app.develop.AppLibrary
import gitfox.entity.app.session.AuthHolder
import gitfox.entity.app.session.OAuthParams
import gitfox.entity.app.session.UserAccount
import gitfox.model.data.cache.ProjectCache
import gitfox.model.data.server.*
import gitfox.model.data.state.ServerChanges
import gitfox.model.data.state.SessionSwitcher
import gitfox.model.data.storage.Prefs
import gitfox.model.interactor.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import platform.Foundation.NSUserDefaults

class SDK(
    private val defaultServerPath: String,
    private val defaultPageSize: Int = 20,
    private val cacheLifetime: Long = 300_000L,
    private val oAuthParams: OAuthParams,
    private val getLibraries: suspend () -> List<AppLibrary>,
    private val appInfo: AppInfo,
    private val isDebug: Boolean
) {
    private val json = Json(JsonConfiguration.Stable.copy(
        ignoreUnknownKeys = true,
        isLenient = true,
        encodeDefaults = false
    ))
    private val projectCache = ProjectCache(cacheLifetime)
    private val httpClientFactory = HttpClientFactory(json)
    private val userAccountApi = UserAccountApi(httpClientFactory.create(null, isDebug))
    private val serverChanges = ServerChanges()
    private val prefs = Prefs(AppleSettings(NSUserDefaults.standardUserDefaults()), json)

    private var userAccount: UserAccount? = null
    private var serverPath: String = defaultServerPath
    private var authHolder: AuthHolder = AuthHolder(null, true)
    private var api: GitlabApi = ApiWithChangesRegistration(
        ApiWithProjectCache(
            GitlabApiImpl(serverPath, httpClientFactory.create(authHolder, isDebug)),
            projectCache
        ),
        serverChanges
    )

    private val sessionSwitcher = object : SessionSwitcher {
        override val hasSession: Boolean = userAccount != null
        override fun initSession(newAccount: UserAccount?) {
            this@SDK.initSession(newAccount)
        }
    }

    private fun initSession(newAccount: UserAccount?) {
        userAccount = newAccount
        if (newAccount != null) {
            authHolder = AuthHolder(newAccount.token, newAccount.isOAuth)
            serverPath = newAccount.serverPath
        } else {
            authHolder = AuthHolder(null, true)
            serverPath = defaultServerPath
        }
        api = ApiWithChangesRegistration(
            ApiWithProjectCache(
                GitlabApiImpl(serverPath, httpClientFactory.create(authHolder, isDebug)),
                projectCache
            ),
            serverChanges
        )
    }

    fun getCurrentServerPath() = serverPath
    fun getAppInfoInteractor() = AppInfoInteractor(getLibraries, appInfo)
    fun getLaunchInteractor() = LaunchInteractor(prefs, sessionSwitcher)
    fun getCommitInteractor() = CommitInteractor(api)
    fun getEventInteractor() = EventInteractor(api, defaultPageSize)
    fun getIssueInteractor() = IssueInteractor(api, serverChanges, defaultPageSize)
    fun getLabelInteractor() = LabelInteractor(api, serverChanges, defaultPageSize)
    fun getMembersInteractor() = MembersInteractor(api, serverChanges, defaultPageSize)
    fun getMergeRequestInteractor() = MergeRequestInteractor(api, serverChanges, defaultPageSize)
    fun getMilestoneInteractor() = MilestoneInteractor(api, serverChanges, defaultPageSize)
    fun getProjectInteractor() = ProjectInteractor(api, serverChanges, defaultPageSize)
    fun getTodoInteractor() = TodoInteractor(api, serverChanges, defaultPageSize)
    fun getUserInteractor() = UserInteractor(api)

    fun getSessionInteractor() = SessionInteractor(
        prefs, oAuthParams, userAccountApi, projectCache, sessionSwitcher
    )

    fun getAccountInteractor() = AccountInteractor(
        serverPath, api, serverChanges, getTodoInteractor(), getMergeRequestInteractor(), getIssueInteractor()
    )
}
package gitfox

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.russhwolf.settings.AppleSettings
import gitfox.adapter.*
import gitfox.client.HttpClientFactory
import gitfox.entity.app.session.OAuthParams
import platform.Foundation.NSUserDefaults

class IosSDK(
    defaultServerPath: String,
    private val defaultPageSize: Int = SDK.defaultPageSize,
    cacheLifetime: Long = SDK.cacheLifetime,
    oAuthParams: OAuthParams,
    isDebug: Boolean
) {

    init {
        if (isDebug) Napier.base(DebugAntilog())
    }

    private val sdk = SDK(
        defaultServerPath,
        defaultPageSize,
        cacheLifetime,
        oAuthParams,
        isDebug,
        HttpClientFactory(),
        AppleSettings(NSUserDefaults.standardUserDefaults())
    )

    fun getLaunchInteractor() = sdk.getLaunchInteractor()
    fun getCommitInteractor() = IosCommitInteractor(sdk.getCommitInteractor())
    fun getEventInteractor() = IosEventInteractor(sdk.getEventInteractor(), defaultPageSize)
    fun getIssueInteractor() = IosIssueInteractor(sdk.getIssueInteractor(), defaultPageSize)
    fun getLabelInteractor() = IosLabelInteractor(sdk.getLabelInteractor())
    fun getMembersInteractor() = IosMembersInteractor(sdk.getMembersInteractor(), defaultPageSize)
    fun getMergeRequestInteractor() = IosMergeRequestInteractor(sdk.getMergeRequestInteractor(), defaultPageSize)
    fun getMilestoneInteractor() = IosMilestoneInteractor(sdk.getMilestoneInteractor(), defaultPageSize)
    fun getProjectInteractor() = IosProjectInteractor(sdk.getProjectInteractor(), defaultPageSize)
    fun getTodoInteractor() = IosTodoInteractor(sdk.getTodoInteractor(), defaultPageSize)
    fun getUserInteractor() = IosUserInteractor(sdk.getUserInteractor())
    fun getSessionInteractor() = IosSessionInteractor(sdk.getSessionInteractor())
    fun getAccountInteractor() = IosAccountInteractor(sdk.getAccountInteractor())

}
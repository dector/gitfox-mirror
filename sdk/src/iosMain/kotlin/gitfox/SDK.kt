package gitfox

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import com.russhwolf.settings.AppleSettings
import gitfox.SDK.Companion.cacheLifetime
import gitfox.SDK.Companion.defaultPageSize
import gitfox.adapter.*
import gitfox.entity.app.session.OAuthParams
import gitfox.util.HttpClientFactory
import gitfox.util.createHttpEngine
import platform.Foundation.NSUserDefaults

class IosSDK(
    oAuthParams: OAuthParams,
    isDebug: Boolean
) {

    init {
        if (isDebug) Napier.base(DebugAntilog())
    }

    private val sdk = SDK(
        defaultPageSize,
        cacheLifetime,
        oAuthParams,
        isDebug,
        HttpClientFactory { cacheSize, timeout -> createHttpEngine(cacheSize, timeout) },
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
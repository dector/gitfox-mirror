package gitfox

import com.github.aakira.napier.Napier
import com.russhwolf.settings.JsSettings
import gitfox.adapter.*
import gitfox.client.HttpClientFactory
import gitfox.entity.app.session.OAuthParams
import gitfox.util.JsAntilog

class JsSDK(
    oAuthParams: OAuthParams,
    isDebug: Boolean
) {

    init {
        if (isDebug) Napier.base(JsAntilog())
    }

    private val sdk = SDK(
        SDK.defaultPageSize,
        SDK.cacheLifetime,
        oAuthParams,
        isDebug,
        HttpClientFactory(),
        JsSettings()
    )

    fun getLaunchInteractor() = sdk.getLaunchInteractor()
    fun getCommitInteractor() = JsCommitInteractor(sdk.getCommitInteractor())
    fun getEventInteractor() = JsEventInteractor(sdk.getEventInteractor(), SDK.defaultPageSize)
    fun getIssueInteractor() = JsIssueInteractor(sdk.getIssueInteractor(), SDK.defaultPageSize)
    fun getLabelInteractor() = JsLabelInteractor(sdk.getLabelInteractor())
    fun getMembersInteractor() = JsMembersInteractor(sdk.getMembersInteractor(), SDK.defaultPageSize)
    fun getMergeRequestInteractor() = JsMergeRequestInteractor(sdk.getMergeRequestInteractor(), SDK.defaultPageSize)
    fun getMilestoneInteractor() = JsMilestoneInteractor(sdk.getMilestoneInteractor(), SDK.defaultPageSize)
    fun getProjectInteractor() = JsProjectInteractor(sdk.getProjectInteractor(), SDK.defaultPageSize)
    fun getTodoInteractor() = JsTodoInteractor(sdk.getTodoInteractor(), SDK.defaultPageSize)
    fun getUserInteractor() = JsUserInteractor(sdk.getUserInteractor())
    fun getSessionInteractor() = JsSessionInteractor(sdk.getSessionInteractor())
    fun getAccountInteractor() = JsAccountInteractor(sdk.getAccountInteractor())
}
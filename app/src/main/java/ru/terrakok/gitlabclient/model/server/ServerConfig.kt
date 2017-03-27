package ru.terrakok.gitlabclient.model.server

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class ServerConfig {
    private val baseUrl= "https://gitlab.com/"
    private val appId = "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d"
    private val authRedirectUrl = "app://gitlab.client/"
    private val redirectCodeTag = "code="
    private val redirectStateTag = "&state="

    fun getAuthUrl(hash: String)
            = "${baseUrl}oauth/authorize?client_id=${appId}&redirect_uri=${authRedirectUrl}&response_type=code&state=${hash}"

    fun checkRedirect(url: String) = url.indexOf(authRedirectUrl) == 0

    fun getCodeFromAuthRedirect(url: String): String {
        val fi = url.indexOf(redirectCodeTag) + redirectCodeTag.length
        val li = url.indexOf(redirectStateTag)
        return url.substring(fi, li)
    }
}
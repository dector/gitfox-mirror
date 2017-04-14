package ru.terrakok.gitlabclient.model.server

import com.google.gson.GsonBuilder
import io.reactivex.Completable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.terrakok.gitlabclient.model.profile.ProfileManager


/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class ServerManager(private val profileManager: ProfileManager, debug: Boolean) {

    //todo remove develop params and add ability for custom domain
    companion object {
        private val SERVER_URL = "https://gitlab.com/"
        private val APP_ID = "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d"
        private val APP_KEY = "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3"
        private val AUTH_REDIRECT_URI = "app://gitlab.client/"
    }

    val domen = SERVER_URL
    val api: GitlabApi

    init {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addNetworkInterceptor(AuthHeaderInterceptor(profileManager))
        if (debug) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
        }

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .baseUrl(SERVER_URL).build()

        api = retrofit.create(GitlabApi::class.java)
    }

    //region auth
    fun getAuthUrl(hash: String)
            = "${SERVER_URL}oauth/authorize" +
            "?client_id=${APP_ID}" +
            "&redirect_uri=${AUTH_REDIRECT_URI}" +
            "&response_type=code&state=${hash}"

    fun checkAuthRedirect(url: String) = url.indexOf(AUTH_REDIRECT_URI) == 0

    fun getCodeFromAuthRedirect(url: String): String {
        val redirectCodeTag = "code="
        val redirectStateTag = "&state="
        val fi = url.indexOf(redirectCodeTag) + redirectCodeTag.length
        val li = url.indexOf(redirectStateTag)
        return url.substring(fi, li)
    }

    fun auth(code: String): Completable =
            api.auth(APP_ID, APP_KEY, code, AUTH_REDIRECT_URI)
                    .doOnSuccess {
                        profileManager.updateToken(it.token)
                        profileManager.refreshProfile()
                    }
                    .toCompletable()

    private class AuthHeaderInterceptor(private val profileManager: ProfileManager) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            profileManager.getToken()?.let {
                request = request.newBuilder().addHeader("Authorization", "Bearer " + it).build()
            }
            return chain.proceed(request)
        }
    }
    //endregion
}
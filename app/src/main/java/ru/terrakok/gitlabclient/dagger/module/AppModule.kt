package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.GitlabApiProvider
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.data.server.interceptor.AuthHeaderInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.CurlLoggingInterceptor
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.system.AppSchedulers
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideResourceManager() = ResourceManager(context)

    @Provides
    @Singleton
    fun provideServerData() = ServerConfig()

    @Provides
    @Singleton
    fun provideAuthData(): AuthHolder = Prefs(context)

    @Provides
    @Singleton
    fun provideSchedulers(): SchedulersProvider = AppSchedulers()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(authData: AuthHolder): OkHttpClient {

        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addNetworkInterceptor(AuthHeaderInterceptor(authData))
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
            httpClientBuilder.addNetworkInterceptor(CurlLoggingInterceptor())
        }

        return httpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideGitlabApi(okHttpClient: OkHttpClient, gson: Gson, serverConfig: ServerConfig): GitlabApi
            = GitlabApiProvider(okHttpClient, gson, serverConfig).api
}
package ru.terrakok.gitlabclient.di.module

import android.content.Context
import gitfox.SDK
import gitfox.entity.app.develop.AppInfo
import gitfox.entity.app.develop.AppLibrary
import gitfox.entity.app.session.OAuthParams
import gitfox.model.interactor.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.AppDevelopersPath
import ru.terrakok.gitlabclient.di.provider.*
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.system.ResourceManager
import ru.terrakok.gitlabclient.system.message.SystemMessageNotifier
import toothpick.config.Module
import java.io.InputStreamReader

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class AppModule(context: Context) : Module() {
    init {
        // Global
        bind(Context::class.java).toInstance(context)
        bind(String::class.java).withName(AppDevelopersPath::class.java).toInstance(BuildConfig.APP_DEVELOPERS_PATH)
        bind(ResourceManager::class.java).singleton()
        bind(SystemMessageNotifier::class.java).toInstance(SystemMessageNotifier())

        // Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        // Error handler with logout logic
        bind(ErrorHandler::class.java).singleton()


        suspend fun getAppLibraries(): List<AppLibrary> = suspendCancellableCoroutine { continuation ->
            context.assets.open("app/app_libraries.json").use { stream ->
                val list: List<AppLibrary> = Json(configuration = JsonConfiguration.Stable).parse(
                    AppLibrary.serializer().list,
                    InputStreamReader(stream).readText()
                )
                continuation.resume(list) {}
            }
        }

        val sdk = SDK(
            context,
            BuildConfig.ORIGIN_GITLAB_ENDPOINT,
            oAuthParams = OAuthParams(
                BuildConfig.ORIGIN_GITLAB_ENDPOINT,
                BuildConfig.OAUTH_APP_ID,
                BuildConfig.OAUTH_SECRET,
                BuildConfig.OAUTH_CALLBACK
            ),
            appInfo = AppInfo(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                BuildConfig.APP_DESCRIPTION,
                BuildConfig.VERSION_UID.take(8),
                BuildConfig.APP_HOME_PAGE,
                BuildConfig.FEEDBACK_URL
            ),
            getLibraries = { getAppLibraries() },
            isDebug = BuildConfig.DEBUG
        )

        bind(AccountInteractor::class.java).toProviderInstance(AccountInteractorProvider(sdk))
        bind(AppInfoInteractor::class.java).toProviderInstance(AppInfoInteractorProvider(sdk))
        bind(CommitInteractor::class.java).toProviderInstance(CommitInteractorProvider(sdk))
        bind(EventInteractor::class.java).toProviderInstance(EventInteractorProvider(sdk))
        bind(IssueInteractor::class.java).toProviderInstance(IssueInteractorProvider(sdk))
        bind(LabelInteractor::class.java).toProviderInstance(LabelInteractorProvider(sdk))
        bind(LaunchInteractor::class.java).toProviderInstance(LaunchInteractorProvider(sdk))
        bind(MembersInteractor::class.java).toProviderInstance(MembersInteractorProvider(sdk))
        bind(MergeRequestInteractor::class.java).toProviderInstance(MergeRequestInteractorProvider(sdk))
        bind(MilestoneInteractor::class.java).toProviderInstance(MilestoneInteractorProvider(sdk))
        bind(ProjectInteractor::class.java).toProviderInstance(ProjectInteractorProvider(sdk))
        bind(SessionInteractor::class.java).toProviderInstance(SessionInteractorProvider(sdk))
        bind(TodoInteractor::class.java).toProviderInstance(TodoInteractorProvider(sdk))
        bind(UserInteractor::class.java).toProviderInstance(UserInteractorProvider(sdk))


        bind(MarkDownConverter::class.java).toProviderInstance(MarkDownConverterProvider(context, { sdk.getCurrentServerPath() }))
    }
}

package ru.terrakok.gitlabclient.di.module

import android.content.Context
import gitfox.SDK
import gitfox.create
import gitfox.entity.app.session.OAuthParams
import gitfox.model.interactor.*
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.AppDevelopersPath
import ru.terrakok.gitlabclient.di.provider.*
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.system.AppInfo
import ru.terrakok.gitlabclient.system.AppInfoInteractor
import ru.terrakok.gitlabclient.system.ResourceManager
import ru.terrakok.gitlabclient.system.message.SystemMessageNotifier
import toothpick.config.Module
import javax.inject.Qualifier

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

        val sdk = SDK.create(
            context,
            oAuthParams = OAuthParams(
                BuildConfig.ORIGIN_GITLAB_ENDPOINT,
                BuildConfig.OAUTH_APP_ID,
                BuildConfig.OAUTH_SECRET,
                BuildConfig.OAUTH_CALLBACK
            ),
            isDebug = BuildConfig.DEBUG
        )
        bind(AppInfoInteractor::class.java).toInstance(AppInfoInteractor(
            context,
            AppInfo(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                BuildConfig.APP_DESCRIPTION,
                BuildConfig.VERSION_UID.take(8),
                BuildConfig.APP_HOME_PAGE,
                BuildConfig.FEEDBACK_URL
            )
        ))

        bind(AccountInteractor::class.java).toProviderInstance(AccountInteractorProvider(sdk))
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


        bind(String::class.java).withName(ServerPath::class.java).toProviderInstance { sdk.getCurrentServerPath() }
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ServerPath
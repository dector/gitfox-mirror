package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.UrlProcessorRelativeToAbsolute
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.spans.SpannableTheme
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.util.color

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 28.02.18.
 */
class MarkDownConverterProvider @Inject constructor(
    private val context: Context,
    private val okHttpClientFactory: OkHttpClientFactory,
    private val tokHolder: AuthHolder,
    private val schedulers: SchedulersProvider,
    @ServerPath private val serverPath: String
) : Provider<MarkDownConverter> {

    private val spannableTheme
        get() = SpannableTheme
            .builderWithDefaults(context)
            .codeBackgroundColor(context.color(R.color.beige))
            .build()

    private val asyncDrawableLoader
        get() = AsyncDrawableLoader.builder()
            .client(okHttpClientFactory.create(tokHolder, false, BuildConfig.DEBUG))
            .executorService(Executors.newCachedThreadPool())
            .resources(context.resources)
            .build()

    private val urlProcessor = UrlProcessorRelativeToAbsolute(serverPath)

    private val spannableConfig
        get() = SpannableConfiguration.builder(context)
            .asyncDrawableLoader(asyncDrawableLoader)
            .urlProcessor(urlProcessor)
            .theme(spannableTheme)
            .build()

    override fun get() = MarkDownConverter(
        spannableConfig,
        schedulers
    )
}

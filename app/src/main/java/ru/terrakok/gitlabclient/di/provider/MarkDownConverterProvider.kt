package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider
import okhttp3.OkHttpClient
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.UrlProcessorRelativeToAbsolute
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.spans.SpannableTheme
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.DefaultServerPath
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 28.02.18.
 */
class MarkDownConverterProvider @Inject constructor(
    private val context: Context,
    private val httpClient: OkHttpClient,
    private val schedulers: SchedulersProvider,
    @DefaultServerPath private val defaultServerPath: String
) : Provider<MarkDownConverter> {

    private val spannableTheme
        get() = SpannableTheme
            .builderWithDefaults(context)
            .codeBackgroundColor(context.color(R.color.beige))
            .build()

    private val asyncDrawableLoader
        get() = AsyncDrawableLoader.builder()
            .client(httpClient)
            .executorService(Executors.newCachedThreadPool())
            .resources(context.resources)
            .build()

    private val urlProcessor = UrlProcessorRelativeToAbsolute(defaultServerPath)

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
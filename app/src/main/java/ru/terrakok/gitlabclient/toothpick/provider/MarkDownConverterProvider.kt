package ru.terrakok.gitlabclient.toothpick.provider

import android.content.Context
import android.graphics.Rect
import okhttp3.OkHttpClient
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.UrlProcessorRelativeToAbsolute
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.renderer.ImageSize
import ru.noties.markwon.renderer.ImageSizeResolver
import ru.noties.markwon.spans.SpannableTheme
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider

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

    private val imageSizeResolver = object : ImageSizeResolver() {
        override fun resolveImageSize(
            imageSize: ImageSize?,
            imageBounds: Rect,
            canvasWidth: Int,
            textSize: Float
        ): Rect {
            // This implementation was taken from SNAPSHOT @2.0,0.
            // Post process bounds to fit canvasWidth (previously was inside AsyncDrawable)
            // must be applied only if imageSize is null.
            val rect: Rect
            val w = imageBounds.width()
            rect = if (w > canvasWidth) {
                val reduceRatio = w.toFloat() / canvasWidth
                Rect(
                    0,
                    0,
                    canvasWidth,
                    (imageBounds.height() / reduceRatio + .5f).toInt()
                )
            } else {
                imageBounds
            }
            return rect
        }
    }

    private val spannableConfig
        get() = SpannableConfiguration.builder(context)
            .asyncDrawableLoader(asyncDrawableLoader)
            .urlProcessor(urlProcessor)
            .theme(spannableTheme)
            .imageSizeResolver(imageSizeResolver)
            .build()

    override fun get() = MarkDownConverter(
        spannableConfig,
        schedulers
    )
}
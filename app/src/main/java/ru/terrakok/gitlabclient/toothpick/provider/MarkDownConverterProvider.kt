package ru.terrakok.gitlabclient.toothpick.provider

import android.content.Context
import okhttp3.OkHttpClient
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.UrlProcessorRelativeToAbsolute
import ru.noties.markwon.il.AsyncDrawableLoader
import ru.noties.markwon.spans.SpannableTheme
import ru.noties.markwon.tasklist.TaskListExtension
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.markwonx.*
import ru.terrakok.gitlabclient.markwonx.label.LabelDecorator
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.label.LabelVisitor
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 28.02.18.
 */
class MarkDownConverterProvider @Inject constructor(
    private val context: Context,
    private val httpClient: OkHttpClient,
    private val schedulers: SchedulersProvider,
    @DefaultServerPath private val defaultServerPath: String
) {

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

    private fun getMarkdownDecorator(labelDescriptions: List<LabelDescription>): MarkdownDecorator {
        return CompositeMarkdownDecorator(
            LabelDecorator(labelDescriptions)
        )
    }

    private fun getParser(labelDescriptions: List<LabelDescription>): Parser {
        return Parser.Builder().apply {
            extensions(
                listOf(
                    StrikethroughExtension.create(),
                    TablesExtension.create(),
                    TaskListExtension.create()
                )
            )

            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.LABEL to LabelExtensionProcessor(labelDescriptions)
                    )
                )
            )
        }.build()
    }

    private fun getCustomVisitor(spannableBuilder: SpannableBuilder): Visitor =
        CompositeVisitor(
            spannableConfig,
            spannableBuilder,
            LabelVisitor(
                spannableConfig,
                spannableBuilder
            )
        )

    fun get(labels: List<Label>): MarkDownConverter {
        val labelDescriptions = labels.map {
            LabelDescription(
                id = it.id,
                name = it.name,
                color = it.color
            )
        }
        return MarkDownConverter(
            spannableConfig,
            getParser(labelDescriptions),
            getMarkdownDecorator(labelDescriptions),
            { builder -> getCustomVisitor(builder) },
            schedulers
        )
    }
}
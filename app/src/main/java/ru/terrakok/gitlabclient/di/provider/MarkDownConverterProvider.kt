package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
import ru.terrakok.gitlabclient.di.DefaultServerPath
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.markwonx.*
import ru.terrakok.gitlabclient.markwonx.label.*
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.markwonx.milestone.SimpleMilestoneVisitor
import ru.terrakok.gitlabclient.model.interactor.label.LabelInteractor
import ru.terrakok.gitlabclient.model.interactor.milestone.MilestoneInteractor
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
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
    private val labelInteractor: LabelInteractor,
    private val milestoneInteractor: MilestoneInteractor,
    private val labelSpanConfig: LabelSpanConfig,
    @DefaultServerPath private val defaultServerPath: String
) : Provider<MarkDownConverter> {

    private val spannableTheme by lazy {
        SpannableTheme
            .builderWithDefaults(context)
            .codeBackgroundColor(context.color(R.color.beige))
            .build()
    }

    private val asyncDrawableLoader by lazy {
        AsyncDrawableLoader.builder()
            .client(httpClient)
            .executorService(Executors.newCachedThreadPool())
            .resources(context.resources)
            .build()
    }

    private val urlProcessor by lazy { UrlProcessorRelativeToAbsolute(defaultServerPath) }

    private val spannableConfig by lazy {
        SpannableConfiguration.builder(context)
            .asyncDrawableLoader(asyncDrawableLoader)
            .urlProcessor(urlProcessor)
            .theme(spannableTheme)
            .build()
    }

    private val markdownDecorator: MarkdownDecorator by lazy {
        CompositeMarkdownDecorator(
            SimpleMarkdownDecorator()
        )
    }

    private val parser: Parser by lazy {
        Parser.Builder().apply {
            extensions(
                listOf(
                    StrikethroughExtension.create(),
                    TablesExtension.create(),
                    TaskListExtension.create()
                )
            )

            val processor = SimpleExtensionProcessor()
            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.LABEL to processor,
                        GitlabMarkdownExtension.MILESTONE to processor
                    )
                )
            )
        }.build()
    }

    private fun getCustomVisitor(labels: List<Label>, milestones: List<Milestone>, spannableBuilder: SpannableBuilder): Visitor {
        val labelDescriptions = labels.map {
            LabelDescription(
                id = it.id,
                name = it.name,
                color = it.color.name
            )
        }
        val milestoneDescriptions = milestones.map {
            MilestoneDescription(
                id = it.iid,
                name = it.title ?: ""
            )
        }
        return CompositeVisitor(
            spannableConfig,
            spannableBuilder,
            SimpleVisitor(
                spannableConfig,
                spannableBuilder,
                mapOf(
                    GitlabMarkdownExtension.LABEL to SimpleLabelVisitor(labelDescriptions, labelSpanConfig),
                    GitlabMarkdownExtension.MILESTONE to SimpleMilestoneVisitor(milestoneDescriptions)
                )
            )
        )
    }

    override fun get(): MarkDownConverter {
        return MarkDownConverter(
            parser,
            markdownDecorator,
            { projectId, builder ->
                Single
                    .zip(labelInteractor.getAllProjectLabels(projectId), milestoneInteractor.getAllProjectMilestones(projectId), BiFunction { labels: List<Label>, milestones: List<Milestone> -> labels to milestones})
                    .map { (labels, milestones) ->
                        getCustomVisitor(labels, milestones, builder)
                    }
            },
            schedulers
        )
    }
}


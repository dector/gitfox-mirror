package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.markwonx.*
import ru.terrakok.gitlabclient.markwonx.label.*
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.markwonx.milestone.SimpleMilestoneVisitor
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import ru.terrakok.gitlabclient.model.interactor.LabelInteractor
import ru.terrakok.gitlabclient.model.interactor.MilestoneInteractor
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.util.color
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 28.02.18.
 */
class MarkDownConverterProvider @Inject constructor(
    private val context: Context,
    private val okHttpClientFactory: OkHttpClientFactory,
    private val tokHolder: AuthHolder,
    private val schedulers: SchedulersProvider,
    private val labelInteractor: LabelInteractor,
    private val milestoneInteractor: MilestoneInteractor,
    private val labelSpanConfig: LabelSpanConfig,
    @ServerPath private val serverPath: String
) : Provider<MarkDownConverter> {

    private val spannableTheme by lazy {
        SpannableTheme
            .builderWithDefaults(context)
            .codeBackgroundColor(context.color(R.color.beige))
            .build()
    }

    private val asyncDrawableLoader by lazy {
        AsyncDrawableLoader.builder()
            .client(okHttpClientFactory.create(tokHolder, false, BuildConfig.DEBUG))
            .executorService(Executors.newCachedThreadPool())
            .resources(context.resources)
            .build()
    }

    private val urlProcessor by lazy { UrlProcessorRelativeToAbsolute(serverPath) }

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

    private fun getCustomVisitor(
        labels: List<Label>,
        milestones: List<Milestone>,
        spannableBuilder: SpannableBuilder,
        markdownClickHandler: MarkdownClickHandler
    ): Visitor {
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
                    GitlabMarkdownExtension.LABEL to SimpleLabelVisitor(
                        labels = labelDescriptions,
                        config = labelSpanConfig,
                        clickHandler = markdownClickHandler
                    ),
                    GitlabMarkdownExtension.MILESTONE to SimpleMilestoneVisitor(
                        milestones = milestoneDescriptions,
                        clickHandler = markdownClickHandler
                    )
                )
            )
        )
    }

    override fun get(): MarkDownConverter {
        return MarkDownConverter(
            parser,
            markdownDecorator,
            { projectId, builder, markdownClickHandler ->
                Single.defer {
                    if (projectId != null) {
                        val allLabels = labelInteractor.getAllProjectLabels(projectId)
                        val allMilestones = milestoneInteractor.getAllProjectMilestones(projectId)
                        Single
                            .zip(allLabels, allMilestones, BiFunction { labels, milestones ->
                                getCustomVisitor(
                                    labels = labels,
                                    milestones = milestones,
                                    spannableBuilder = builder,
                                    markdownClickHandler = markdownClickHandler
                                )
                            })
                    } else {
                        Single.fromCallable {
                            getCustomVisitor(
                                labels = emptyList(),
                                milestones = emptyList(),
                                spannableBuilder = builder,
                                markdownClickHandler = markdownClickHandler
                            )
                        }
                    }
                }
            },
            schedulers
        )
    }
}

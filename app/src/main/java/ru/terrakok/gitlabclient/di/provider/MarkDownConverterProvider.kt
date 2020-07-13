package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import gitfox.entity.Label
import gitfox.entity.Milestone
import gitfox.model.interactor.LabelInteractor
import gitfox.model.interactor.MilestoneInteractor
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
import ru.terrakok.gitlabclient.di.module.ServerPath
import ru.terrakok.gitlabclient.markwonx.CompositeMarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.CompositeVisitor
import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickMediator
import ru.terrakok.gitlabclient.markwonx.MarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelSpanConfig
import ru.terrakok.gitlabclient.markwonx.label.SimpleExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.label.SimpleLabelVisitor
import ru.terrakok.gitlabclient.markwonx.label.SimpleMarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.label.SimpleVisitor
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.markwonx.milestone.SimpleMilestoneVisitor
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
    private val labelInteractor: LabelInteractor,
    private val milestoneInteractor: MilestoneInteractor,
    private val labelSpanConfig: LabelSpanConfig,
    private val markdownClickMediator: MarkdownClickMediator,
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
        spannableBuilder: SpannableBuilder
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
                    GitlabMarkdownExtension.LABEL to SimpleLabelVisitor(labelDescriptions, labelSpanConfig, markdownClickMediator),
                    GitlabMarkdownExtension.MILESTONE to SimpleMilestoneVisitor(milestoneDescriptions, markdownClickMediator)
                )
            )
        )
    }

    override fun get(): MarkDownConverter {
        return MarkDownConverter(
            parser,
            markdownDecorator
        ) { projectId, builder ->
            if (projectId != null) {
                val allLabels = labelInteractor.getAllProjectLabels(projectId)
                val allMilestones = milestoneInteractor.getAllProjectMilestones(projectId)
                getCustomVisitor(allLabels, allMilestones, builder)
            } else {
                getCustomVisitor(emptyList(), emptyList(), builder)
            }
        }
    }
}

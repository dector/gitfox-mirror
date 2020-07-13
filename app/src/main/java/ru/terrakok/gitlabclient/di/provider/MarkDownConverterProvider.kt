package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import com.github.aakira.napier.Napier
import gitfox.entity.Label
import gitfox.entity.Milestone
import gitfox.model.interactor.LabelInteractor
import gitfox.model.interactor.MilestoneInteractor
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import io.noties.markwon.image.glide.GlideImagesPlugin
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.module.ServerPath
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelSpanConfig
import ru.terrakok.gitlabclient.markwonx.label.SimpleLabelVisitor
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.markwonx.milestone.SimpleMilestoneVisitor
import ru.terrakok.gitlabclient.markwonx.simple.SimpleNodeVisitor
import ru.terrakok.gitlabclient.markwonx.simple.SimplePlugin
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.util.color
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
    @ServerPath private val serverPath: String
) : Provider<MarkDownConverter> {

    private val urlProcessor by lazy { ImageDestinationProcessorRelativeToAbsolute(serverPath) }

    private fun createNodeVisitors(
        labels: List<Label>,
        milestones: List<Milestone>,
        markdownClickHandler: MarkdownClickHandler
    ): Map<GitlabMarkdownExtension, SimpleNodeVisitor> {
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

        return mapOf(
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
    }

    private fun createMarkwon(nodeVisitors: Map<GitlabMarkdownExtension, SimpleNodeVisitor>): Markwon {
        return Markwon.builder(context).apply {
            usePlugins(
                listOf(
                    object : AbstractMarkwonPlugin() {
                        override fun configureTheme(builder: MarkwonTheme.Builder) {
                            builder
                                .codeBackgroundColor(context.color(R.color.beige))
                        }

                        override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                            builder
                                .imageDestinationProcessor(urlProcessor)
                        }
                    },
                    GlideImagesPlugin.create(context),
                    HtmlPlugin.create(),
                    StrikethroughPlugin.create(),
                    TablePlugin.create(context),
                    TaskListPlugin.create(context),
                    SimplePlugin(nodeVisitors)
                )
            )

        }.build()
    }

    override fun get(): MarkDownConverter {
        return MarkDownConverter(
            this::createMarkwon
        )
    }

    private suspend fun createMarkwon(
        projectId: Long?,
        markdownClickHandler: MarkdownClickHandler
    ): Markwon {
        val (labels: List<Label>, milestones: List<Milestone>) =
            if (projectId != null) {
                val allLabels = kotlin.runCatching {
                    labelInteractor
                        .getAllProjectLabels(projectId)
                }.getOrElse {
                    Napier.e("", it)
                    emptyList()
                }
                val allMilestones = kotlin.runCatching {
                    milestoneInteractor
                        .getAllProjectMilestones(projectId)
                }.getOrElse {
                    Napier.e("", it)
                    emptyList()
                }
                allLabels to allMilestones
            } else {
                emptyList<Label>() to emptyList<Milestone>()
            }
        return createMarkwon(
            createNodeVisitors(
                labels = labels,
                milestones = milestones,
                markdownClickHandler = markdownClickHandler
            )
        )
    }
}

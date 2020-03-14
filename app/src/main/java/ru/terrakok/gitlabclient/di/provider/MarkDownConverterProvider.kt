package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.glide.GlideImagesPlugin
import io.noties.markwon.urlprocessor.UrlProcessorRelativeToAbsolute
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickHandler
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelSpanConfig
import ru.terrakok.gitlabclient.markwonx.label.SimpleLabelVisitor
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.markwonx.milestone.SimpleMilestoneVisitor
import ru.terrakok.gitlabclient.markwonx.simple.SimpleNodeVisitor
import ru.terrakok.gitlabclient.markwonx.simple.SimplePlugin
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import ru.terrakok.gitlabclient.model.interactor.LabelInteractor
import ru.terrakok.gitlabclient.model.interactor.MilestoneInteractor
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.util.color
import timber.log.Timber
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

    private val urlProcessor by lazy { UrlProcessorRelativeToAbsolute(serverPath) }

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
                                .urlProcessor(urlProcessor)
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
            this::createMarkwon,
            schedulers
        )
    }

    private fun createMarkwon(
        projectId: Long?,
        markdownClickHandler: MarkdownClickHandler
    ): Single<Markwon> {
        return Single
            .defer {
                if (projectId != null) {
                    val allLabels =
                        labelInteractor
                            .getAllProjectLabels(projectId)
                            .doOnError { Timber.e(it) }
                            .onErrorReturn { emptyList() }
                    val allMilestones =
                        milestoneInteractor
                            .getAllProjectMilestones(projectId)
                            .doOnError { Timber.e(it) }
                            .onErrorReturn { emptyList() }
                    Single
                        .zip(
                            allLabels,
                            allMilestones,
                            BiFunction { labels, milestones -> labels to milestones }
                        )
                } else {
                    Single.fromCallable { emptyList<Label>() to emptyList<Milestone>() }
                }
            }
            .map { (labels, milestones) ->
                createMarkwon(
                    createNodeVisitors(
                        labels = labels,
                        milestones = milestones,
                        markdownClickHandler = markdownClickHandler
                    )
                )
            }
    }
}

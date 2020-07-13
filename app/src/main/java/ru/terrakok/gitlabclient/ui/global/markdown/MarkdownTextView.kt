package ru.terrakok.gitlabclient.ui.global.markdown

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.github.aakira.napier.Napier
import io.noties.markwon.Markwon
import io.noties.markwon.image.glide.GlideImagesPlugin
import moxy.MvpDelegate
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.provider.LabelSpanConfigProvider
import ru.terrakok.gitlabclient.di.provider.MarkDownConverterProvider
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.MarkdownClickListener
import ru.terrakok.gitlabclient.markwonx.label.LabelSpanConfig
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.markdown.MarkdownPresenter
import ru.terrakok.gitlabclient.presentation.markdown.MarkdownView
import toothpick.Toothpick
import toothpick.config.Module

class MarkdownTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(
    context,
    attrs,
    defStyleAttr
), MarkdownView {

    private lateinit var parentDelegate: MvpDelegate<*>
    private val mvpDelegate: MvpDelegate<MarkdownTextView> by lazy {
        MvpDelegate(this).apply {
            setParentDelegate(parentDelegate, hashCode().toString())
        }
    }

    private val markwon by lazy {
        Markwon
            .builder(context)
            .usePlugin(GlideImagesPlugin.create(context))
            .build()
    }

    @InjectPresenter
    internal lateinit var presenter: MarkdownPresenter

    private val markdownClickListeners by lazy { mutableListOf<MarkdownClickListener>() }

    @ProvidePresenter
    fun providePresenter(): MarkdownPresenter = Toothpick
        .openScopes(DI.APP_SCOPE, "MarkdownTextView_${hashCode()}")
        .apply {
            installModules(object : Module() {
                init {
                    bind(LabelSpanConfig::class.java).toProvider(LabelSpanConfigProvider::class.java)
                    bind(MarkDownConverter::class.java).toProvider(MarkDownConverterProvider::class.java)
                }
            })
        }
        .getInstance(MarkdownPresenter::class.java)

    override fun markdownClicked(extension: GitlabMarkdownExtension, value: Any) {
        val eventConsumed = markdownClickListeners.any { it.onMarkdownClick(extension, value) }
        if (!eventConsumed) {
            presenter.markdownClicked(extension, value)
        }
    }

    fun initWithParentDelegate(parentDelegate: MvpDelegate<*>) {
        this.parentDelegate = parentDelegate

        mvpDelegate.onCreate()
        mvpDelegate.onAttach()
    }

    fun setMarkdown(markdown: String?, projectId: Long?) {
        text = null
        if (markdown != null) {
            presenter.setMarkdown(markdown, projectId)
        } else {
            Napier.e("Text in markdown text view ${if (id != NO_ID) resources.getResourceName(id) else ""} is null")
        }
    }

    override fun setMarkdownText(text: Spanned) {
        markwon.setParsedMarkdown(this, text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mvpDelegate.onSaveInstanceState()
        mvpDelegate.onDetach()
    }
}
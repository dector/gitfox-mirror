package ru.terrakok.gitlabclient.ui.global.markdown

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.presentation.markdown.MarkdownPresenter
import ru.terrakok.gitlabclient.presentation.markdown.MarkdownView
import timber.log.Timber
import toothpick.Toothpick

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

    @InjectPresenter
    internal lateinit var presenter: MarkdownPresenter

    @ProvidePresenter
    fun providePresenter(): MarkdownPresenter = Toothpick
        .openScope(DI.SERVER_SCOPE)
        .getInstance(MarkdownPresenter::class.java)

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
            Timber.e("Text in markdown text view ${if (id != NO_ID) resources.getResourceName(id) else ""} is null")
        }
    }

    override fun setMarkdownText(text: CharSequence) {
        Markwon.setText(this, text)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        mvpDelegate.onSaveInstanceState()
        mvpDelegate.onDetach()
    }

}
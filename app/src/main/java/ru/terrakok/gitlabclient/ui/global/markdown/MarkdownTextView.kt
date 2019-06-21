package ru.terrakok.gitlabclient.ui.global.markdown

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.presentation.markdown.MarkdownPresenter
import ru.terrakok.gitlabclient.presentation.markdown.MarkdownView
import timber.log.Timber
import toothpick.Toothpick

class MarkdownTextView : TextView, MarkdownView {

    @JvmOverloads
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

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

    fun setMarkdown(markdown: String?, projectId: Long) {
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
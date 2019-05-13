package ru.terrakok.gitlabclient.ui.global.view.custom.code_highlight

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class CodeHighlightView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    private var onCodeHighlightListener: OnCodeHighlightListener? = null

    fun setOnCodeHighlightProgressLister(onCodeHighlightListener: OnCodeHighlightListener) {
        this.onCodeHighlightListener = onCodeHighlightListener
    }

    init {
        loadUrl(EMPTY_PAGE)
        settings.apply {
            javaScriptEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url != EMPTY_PAGE) {
                    onCodeHighlightListener?.onCodeHighlightStarted()
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != EMPTY_PAGE) {
                    onCodeHighlightListener?.onCodeHighlightFinished()
                }
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (view.url != EMPTY_PAGE && newProgress == PROGRESS_PAGE_LOADED) {
                    onCodeHighlightListener?.onCodeHighlightFinished()
                }
            }
        }
    }

    fun highlightCode(rawFile: String) {
        val jsPage = CodeHighlightJsPage.generatePage(rawFile, "default.css")
        loadDataWithBaseURL("file:///android_asset/code_highlight/", jsPage, "text/html", "utf-8", null)
    }

    companion object {
        private const val EMPTY_PAGE = "about:blank"
        private const val PROGRESS_PAGE_LOADED = 100
    }

    interface OnCodeHighlightListener {
        fun onCodeHighlightStarted()
        fun onCodeHighlightFinished()
    }
}
package ru.terrakok.gitlabclient.ui.global.view.custom.code_highlight

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient

class CodeHighlightView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr) {

    // TODO: project file details (Implement the next steps):
    // 1) Progress logic - highlighting code is rather long operation
    // 2) Lifecycle logic
    // 3) Cache content logic - ?
    // 4) Style logic - support two styles (default, darkula).

    init {
        loadUrl(EMPTY_PAGE)
        getSettings().apply {
            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.setSupportZoom(true)
            settings.displayZoomControls = false
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        }

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != EMPTY_PAGE) {
                    // TODO: code highlight (Hide progress).
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url != EMPTY_PAGE) {
                    // TODO: code highlight (Show progress).
                }
            }
        }
    }

    fun highlightRawCode(code: String) {
        val jsPage = CodeHighlightJsPage.generatePage(code, "default.css")
        loadDataWithBaseURL("file:///android_asset/code_highlight/", jsPage, "text/html", "utf-8", null)
    }

    companion object {
        private const val EMPTY_PAGE = "about:blank"
    }
}
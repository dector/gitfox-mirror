/*
* Modifications copyright (C) 2018 https://github.com/PDDStudio/highlightjs-android
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ru.terrakok.gitlabclient.ui.global.code_highlight

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
        //set the settings for the view
        val settings = getSettings()
        settings.javaScriptEnabled = true
        settings.builtInZoomControls = true
        settings.setSupportZoom(true)
        //disable zoom controls on +Honeycomb devices
        settings.displayZoomControls = false
        //to remove padding and margin
        scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != EMPTY_PAGE) {
                    // hide progress
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                if (url != EMPTY_PAGE) {
                    // start progres
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
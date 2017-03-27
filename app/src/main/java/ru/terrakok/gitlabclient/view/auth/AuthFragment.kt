package ru.terrakok.gitlabclient.view.auth

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.auth.AuthPresenter
import ru.terrakok.gitlabclient.presentation.auth.AuthView
import ru.terrakok.gitlabclient.view.global.BaseFragment


/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class AuthFragment : BaseFragment(), AuthView {

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    override fun getLayoutId() = R.layout.fragment_auth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        val settings = webView.settings
        settings.javaScriptEnabled = true

        webView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showProgressView(true)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showProgressView(false)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return overrideUrlLoading(view, url)
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return overrideUrlLoading(view, request.url.toString())
            }

            private fun overrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun showProgress(isVisible: Boolean) {
        showProgressView(isVisible)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        if (!isProgress()) {
            if (webView.canGoBack()) webView.goBack()
            else presenter.onBackPressed()
        }
    }
}
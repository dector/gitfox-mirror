package ru.terrakok.gitlabclient.ui.auth

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.auth.AuthPresenter
import ru.terrakok.gitlabclient.presentation.auth.AuthView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class AuthFragment : BaseFragment(), AuthView {

    override val layoutRes = R.layout.fragment_auth

    @InjectPresenter lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter {
        return Toothpick
                .openScope(DI.SERVER_SCOPE)
                .getInstance(AuthPresenter::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        CookieManager.getInstance().removeAllCookie()
        webView.settings.javaScriptEnabled = true

        webView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showProgressDialog(true)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showProgressDialog(false)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return overrideUrlLoading(view, url)
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return overrideUrlLoading(view, request.url.toString())
            }

            private fun overrideUrlLoading(view: WebView, url: String): Boolean {
                return presenter.onRedirect(url)
            }
        })
    }

    override fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun showProgress(isVisible: Boolean) {
        showProgressDialog(isVisible)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else presenter.onBackPressed()
    }
}
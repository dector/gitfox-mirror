package ru.terrakok.gitlabclient.ui.auth

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.fragment_auth.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.auth.AuthPresenter
import ru.terrakok.gitlabclient.presentation.auth.AuthView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.util.addSystemBottomPadding
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.showSnackMessage
import ru.terrakok.gitlabclient.util.visible

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
class AuthFragment : BaseFragment(), AuthView, CustomServerAuthFragment.OnClickListener {

    override val layoutRes = R.layout.fragment_auth
    override val customLogin = { url: String, token: String -> presenter.loginOnCustomServer(url, token) }

    @InjectPresenter
    lateinit var presenter: AuthPresenter

    @ProvidePresenter
    fun providePresenter(): AuthPresenter =
            scope.getInstance(AuthPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.addSystemBottomPadding()
        toolbar.apply {
            setNavigationOnClickListener { presenter.onBackPressed() }
            addSystemTopPadding()
            inflateMenu(R.menu.custom_auth_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.customAuthAction -> CustomServerAuthFragment().show(childFragmentManager, null)
                }
                true
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null)
        } else {
            CookieManager.getInstance().removeAllCookie()
        }

        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                showProgressDialog(true)
                showEmptyView(false)
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

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                showEmptyView(true)
            }
        }

        emptyView.setRefreshListener { presenter.refresh() }

        if (savedInstanceState == null) {
            with(webView.settings) {
                javaScriptEnabled = true
                userAgentString = BuildConfig.WEB_AUTH_USER_AGENT
            }
            presenter.coldStart()
        } else {
            webView.restoreState(savedInstanceState)
        }
    }

    private fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyError() else hide() }
        webView.visible(!show)
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onDestroyView() {
        webView.destroy()
        super.onDestroyView()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else presenter.onBackPressed()
    }
}

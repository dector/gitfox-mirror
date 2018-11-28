package ru.terrakok.gitlabclient.ui.privacypolicy

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_privacy_policy.*
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.09.18.
 */
class PrivacyPolicyFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_privacy_policy

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        okButton.setOnClickListener { onBackPressed() }
        webView.loadUrl("https://gitlab.com/terrakok/gitlab-client/raw/develop/PrivacyPolicy.txt")
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

    override fun onBackPressed() {
        super.onBackPressed()
        router.exit()
    }
}
package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import javax.inject.Inject
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.DI
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.09.18.
 */
class StubFragment : BaseFragment() {
    override val parentScopeName = DI.APP_SCOPE

    override val layoutRes = R.layout.fragment_stub

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onBackPressed() {
        router.exit()
    }
}

package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.09.18.
 */
class StubFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_stub

    @Inject
    lateinit var router: FlowRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, scope)
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        router.exit()
    }
}
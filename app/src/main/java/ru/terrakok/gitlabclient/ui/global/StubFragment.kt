package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.09.18.
 */
class StubFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_stub

    private val scopeName: String by argument(ARG_SCOPE)

    @Inject
    lateinit var router: FlowRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(scopeName))
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        router.exit()
    }

    companion object {
        private const val ARG_SCOPE = "arg_scope"
        fun create(scope: String) = StubFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_SCOPE, scope)
            }
        }
    }
}
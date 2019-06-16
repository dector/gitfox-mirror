package ru.terrakok.gitlabclient.ui.my.todos

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_todos_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.10.17
 */
class MyTodosContainerFragment : BaseFragment() {

    @Inject
    lateinit var router: FlowRouter

    @Inject
    lateinit var menuController: GlobalMenuController

    private val adapter: MyTodosPagerAdapter by lazy { MyTodosPagerAdapter() }

    override val layoutRes = R.layout.fragment_my_todos_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { menuController.open() }
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        router.exit()
    }

    private inner class MyTodosPagerAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> Screens.MyTodos(true).fragment
            else -> Screens.MyTodos(false).fragment
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.my_todos_pending)
            1 -> getString(R.string.my_todos_done)
            else -> null
        }
    }
}
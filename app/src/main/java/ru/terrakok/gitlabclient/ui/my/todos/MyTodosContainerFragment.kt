package ru.terrakok.gitlabclient.ui.my.todos

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_todos_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.10.17
 */
class MyTodosContainerFragment : BaseFragment() {

    @Inject lateinit var menuController: GlobalMenuController

    private val adapter: MyTodosPagerAdapter by lazy { MyTodosPagerAdapter() }

    override val layoutRes = R.layout.fragment_my_todos_container

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.MAIN_ACTIVITY_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { menuController.open() }
        viewPager.adapter = adapter
    }

    private inner class MyTodosPagerAdapter : FragmentPagerAdapter(childFragmentManager) {
        private val pages = listOf<Fragment>(
                MyTodosFragment.newInstance(true),
                MyTodosFragment.newInstance(false)
        )
        private val titles = listOf<String>(
                getString(R.string.my_todos_pending),
                getString(R.string.my_todos_done)
        )

        override fun getItem(position: Int) = pages[position]

        override fun getCount() = pages.size

        override fun getPageTitle(position: Int) = titles[position]
    }
}
package ru.terrakok.gitlabclient.ui.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_nav_drawer.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerPresenter
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
class NavigationDrawerFragment : BaseFragment(), NavigationDrawerView {

    @InjectPresenter lateinit var presenter: NavigationDrawerPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_nav_drawer, container, false)

    override fun showVersionName(version: String) {
        versionTV.text = version
    }

    override fun selectMenuItem(id: Int) {
        TODO("not implemented")
    }
}
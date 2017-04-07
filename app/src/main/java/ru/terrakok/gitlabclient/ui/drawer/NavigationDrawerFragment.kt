package ru.terrakok.gitlabclient.ui.drawer

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.fragment_nav_drawer.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerPresenter
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.launch.MainActivity

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
class NavigationDrawerFragment : BaseFragment(), NavigationDrawerView {
    @InjectPresenter lateinit var presenter: NavigationDrawerPresenter

    private var mainActivity: MainActivity? = null
    private val itemClickListener = { view: View ->
        mainActivity?.openNavDrawer(false)
        presenter.onMenuItemClick(view.tag as MenuItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_nav_drawer, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        projectsMI.tag = PROJECTS
        activityMI.tag = ACTIVITY
        groupsMI.tag = GROUPS
        settingsMI.tag = SETTINGS
        aboutMI.tag = ABOUT

        projectsMI.setOnClickListener(itemClickListener)
        activityMI.setOnClickListener(itemClickListener)
        groupsMI.setOnClickListener(itemClickListener)
        settingsMI.setOnClickListener(itemClickListener)
        aboutMI.setOnClickListener(itemClickListener)
    }

    override fun showVersionName(version: String) {
        versionTV.text = version
    }

    override fun showUserInfo(user: User, serverUrl: String) {
        nickTV.text = user.name
        serverNameTV.text = serverUrl
        letterTV.text = user.name?.let {
            it.first().toString()
        }
        Glide.with(avatarIV.context)
                .load(user.avatarUrl)
                .asBitmap()
                .centerCrop()
                .into(object : BitmapImageViewTarget(avatarIV) {
                    override fun setResource(resource: Bitmap?) {
                        resource?.let {
                            avatarIV.visibility = View.VISIBLE
                            RoundedBitmapDrawableFactory.create(view.resources, it).run {
                                this.isCircular = true
                                avatarIV.setImageDrawable(this)
                            }
                        }
                    }
                })
    }

    override fun selectMenuItem(item: MenuItem) {
        for (i in 0..navDrawerMenuContainer.childCount - 1) {
            val menuItem = navDrawerMenuContainer.getChildAt(i)
            menuItem.tag?.let {
                menuItem.isSelected = it == item
            }
        }
    }

    fun onScreenChanged(item: MenuItem) {
        presenter.onScreenChanged(item)
    }
}
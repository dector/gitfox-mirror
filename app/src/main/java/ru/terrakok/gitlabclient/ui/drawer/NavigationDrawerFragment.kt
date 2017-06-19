package ru.terrakok.gitlabclient.ui.drawer

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.fragment_nav_drawer.*
import kotlinx.android.synthetic.main.layout_avatar.*
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.MyUserInfo
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerPresenter
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.launch.MainActivity
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
class NavigationDrawerFragment : BaseFragment(), NavigationDrawerView {
    @InjectPresenter lateinit var presenter: NavigationDrawerPresenter
    @ProvidePresenter
    fun providePresenter() = NavigationDrawerPresenter().also { Toothpick.inject(it, App.APP_SCOPE) }

    override val layoutRes = R.layout.fragment_nav_drawer

    private var mainActivity: MainActivity? = null
    private val itemClickListener = { view: View ->
        mainActivity?.openNavDrawer(false)
        presenter.onMenuItemClick(view.tag as MenuItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        logoutIV.setOnClickListener {
            mainActivity?.openNavDrawer(false)
            presenter.onLogoutClick()
        }

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

    override fun showUserInfo(user: MyUserInfo) {
        if (user.user == null) {
            nickTV.text = ""
            serverNameTV.text = ""
            letterTV.text = ""
            avatarIV.visibility = View.GONE
        } else with(user.user) {
            nickTV.text = this.name
            serverNameTV.text = user.serverName
            letterTV.text = this.name?.let {
                it.first().toString().toUpperCase()
            }
            Glide.with(avatarIV.context)
                    .load(this.avatarUrl)
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
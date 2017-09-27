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
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.model.interactor.profile.MyUserInfo
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerPresenter
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ConfirmDialog
import ru.terrakok.gitlabclient.ui.launch.MainActivity
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
class NavigationDrawerFragment : BaseFragment(), NavigationDrawerView, ConfirmDialog.OnClickListener {
    override val layoutRes = R.layout.fragment_nav_drawer
    private var mainActivity: MainActivity? = null

    private val itemClickListener = { view: View ->
        presenter.onMenuItemClick(view.tag as MenuItem)
    }

    override val dialogConfirm: (tag: String) -> Unit = { tag ->
        when (tag) {
            CONFIRM_LOGOUT_TAG -> presenter.onLogoutClick()
        }
    }

    @InjectPresenter lateinit var presenter: NavigationDrawerPresenter

    @ProvidePresenter
    fun providePresenter(): NavigationDrawerPresenter {
        return Toothpick
                .openScope(DI.MAIN_ACTIVITY_SCOPE)
                .getInstance(NavigationDrawerPresenter::class.java)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        logoutIV.setOnClickListener {
            ConfirmDialog
                    .newInstants(
                            msg = getString(R.string.logout_question),
                            positive = getString(R.string.exit),
                            tag = CONFIRM_LOGOUT_TAG
                    )
                    .show(childFragmentManager, CONFIRM_LOGOUT_TAG)
        }

        activityMI.tag = ACTIVITY
        projectsMI.tag = PROJECTS
        aboutMI.tag = ABOUT

        activityMI.setOnClickListener(itemClickListener)
        projectsMI.setOnClickListener(itemClickListener)
        aboutMI.setOnClickListener(itemClickListener)
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
            letterTV.text = this.name?.first()?.toString()?.toUpperCase()
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
        (0 until navDrawerMenuContainer.childCount)
                .map { navDrawerMenuContainer.getChildAt(it) }
                .forEach { menuItem -> menuItem.tag?.let { menuItem.isSelected = it == item } }
    }

    fun onScreenChanged(item: MenuItem) {
        presenter.onScreenChanged(item)
    }

    private companion object {
        private val CONFIRM_LOGOUT_TAG = "confirm_logout_tag"
    }
}
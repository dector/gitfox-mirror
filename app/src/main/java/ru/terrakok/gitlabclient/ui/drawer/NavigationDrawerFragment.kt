package ru.terrakok.gitlabclient.ui.drawer

import android.content.Context
import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_nav_drawer.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.user.MyUserInfo
import ru.terrakok.gitlabclient.extension.loadRoundedImage
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

    private var userId: Long? = null

    private val itemClickListener = { view: View ->
        presenter.onMenuItemClick(view.tag as MenuItem)
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

        avatarImageView.setOnClickListener { userId?.let { presenter.onUserClick(it) } }

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
            userId = null
            nickTV.text = ""
            serverNameTV.text = ""
            avatarImageView.visibility = View.GONE
        } else {
            with(user.user) {
                userId = this.id
                nickTV.text = this.name
                serverNameTV.text = user.serverName
                avatarImageView.loadRoundedImage(this.avatarUrl, context)
            }
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

    override fun dialogConfirm(tag: String) {
        when (tag) {
            CONFIRM_LOGOUT_TAG -> presenter.onLogoutClick()
        }
    }

    private companion object {
        private val CONFIRM_LOGOUT_TAG = "confirm_logout_tag"
    }
}
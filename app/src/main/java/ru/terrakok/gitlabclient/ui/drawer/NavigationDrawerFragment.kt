package ru.terrakok.gitlabclient.ui.drawer

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_nav_drawer.*
import kotlinx.android.synthetic.main.item_user_acount.view.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerPresenter
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.MessageDialogFragment
import ru.terrakok.gitlabclient.ui.global.view.custom.bindUserAccount
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.inflate
import ru.terrakok.gitlabclient.util.visible

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
class NavigationDrawerFragment : BaseFragment(), NavigationDrawerView, MessageDialogFragment.OnClickListener {
    override val layoutRes = R.layout.fragment_nav_drawer

    private val itemClickListener = { view: View ->
        presenter.onMenuItemClick(view.tag as MenuItem)
    }

    @InjectPresenter
    lateinit var presenter: NavigationDrawerPresenter

    @ProvidePresenter
    fun providePresenter(): NavigationDrawerPresenter =
        scope.getInstance(NavigationDrawerPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showAccountsList(false)
        headerConstraintLayout.addSystemTopPadding()
        avatarView.setOnClickListener { presenter.onUserClick() }
        dropDownImageView.setOnClickListener {
            showAccountsList(accountsContainer.visibility == View.GONE)
        }

        logoutIV.setOnClickListener {
            MessageDialogFragment.create(
                message = getString(R.string.logout_question),
                positive = getString(R.string.exit),
                negative = getString(R.string.cancel),
                tag = CONFIRM_LOGOUT_TAG
            ).show(childFragmentManager, CONFIRM_LOGOUT_TAG)
        }

        activityMI.tag = ACTIVITY
        projectsMI.tag = PROJECTS
        aboutMI.tag = ABOUT

        activityMI.setOnClickListener(itemClickListener)
        projectsMI.setOnClickListener(itemClickListener)
        aboutMI.setOnClickListener(itemClickListener)
    }

    override fun selectMenuItem(item: MenuItem) {
        (0 until navDrawerMenuContainer.childCount)
            .map { navDrawerMenuContainer.getChildAt(it) }
            .forEach { menuItem -> menuItem.tag?.let { menuItem.isSelected = it == item } }
    }

    override fun setAccounts(accounts: List<UserAccount>, currentAccount: UserAccount) {
        nickTV.text = currentAccount.userName
        serverNameTV.text = currentAccount.serverPath
        avatarView.bindUserAccount(currentAccount)

        accountsContainer.removeAllViews()
        accounts.forEach { acc ->
            accountsContainer.inflate(R.layout.item_user_acount)
                .apply {
                    avatarImageView.bindUserAccount(acc, false)
                    nameTextView.text = acc.userName
                    serverTextView.text = acc.serverPath
                    selectorView.visible(acc == currentAccount)
                    setOnClickListener { presenter.onAccountClick(acc) }
                }
                .also {
                    accountsContainer.addView(it)
                }
        }
        accountsContainer.inflate(R.layout.item_add_acount)
            .apply {
                setOnClickListener { presenter.onAddAccountClick() }
            }
            .also {
                accountsContainer.addView(it)
            }
    }

    private fun showAccountsList(show: Boolean) {
        accountsContainer.visible(show)
        dropDownImageView.rotation = if (show) 180f else 0f
    }

    fun onScreenChanged(item: MenuItem) {
        presenter.onScreenChanged(item)
    }

    override fun dialogPositiveClicked(tag: String) {
        when (tag) {
            CONFIRM_LOGOUT_TAG -> presenter.onLogoutClick()
        }
    }

    private companion object {
        private const val CONFIRM_LOGOUT_TAG = "confirm_logout_tag"
    }
}

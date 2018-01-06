package ru.terrakok.gitlabclient.ui.user.info

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_user_info.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.extension.loadRoundedImage
import ru.terrakok.gitlabclient.extension.shareText
import ru.terrakok.gitlabclient.extension.showTextOrHide
import ru.terrakok.gitlabclient.extension.tryOpenLink
import ru.terrakok.gitlabclient.presentation.user.UserInfoPresenter
import ru.terrakok.gitlabclient.presentation.user.UserInfoView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserInfoFragment : BaseFragment(), UserInfoView {

    override val layoutRes = R.layout.fragment_user_info
    private var user: User? = null

    @InjectPresenter lateinit var presenter: UserInfoPresenter

    @ProvidePresenter
    fun providePresenter(): UserInfoPresenter =
            Toothpick
                    .openScope(DI.USER_SCOPE)
                    .getInstance(UserInfoPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        toolbar.inflateMenu(R.menu.share_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.shareAction -> shareText(user?.webUrl)
            }
            true
        }

        userCompanyTextView.setOnClickListener { tryOpenLink(user?.organization) }
        userLocationTextView.setOnClickListener { tryOpenLink(user?.location) }
        userSiteTextView.setOnClickListener { tryOpenLink(user?.websiteUrl) }
        userSkypeTextView.setOnClickListener { tryOpenLink(user?.skype, "skype:") }
        userLinkedinTextView.setOnClickListener { tryOpenLink(user?.linkedin, "https://www.linkedin.com/in/") }
        userTwitterTextView.setOnClickListener { tryOpenLink(user?.twitter, "https://www.twitter.com/") }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun showUser(user: User) {
        this.user = user
        toolbar.title = user.username
        avatarImageView.loadRoundedImage(user.avatarUrl, context)
        usernameTextView.text = user.name
        userIdTextView.text = "@${user.username}"

        userBioTextView.showTextOrHide(user.bio)
        userCompanyTextView.showTextOrHide(user.organization)
        userLocationTextView.showTextOrHide(user.location)
        userSiteTextView.showTextOrHide(user.websiteUrl)
        userSkypeTextView.showTextOrHide(user.skype)
        userLinkedinTextView.showTextOrHide(user.linkedin)
        userTwitterTextView.showTextOrHide(user.twitter)
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(msg: String) {
        showSnackMessage(msg)
    }
}
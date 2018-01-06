package ru.terrakok.gitlabclient.ui.project.info

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_info.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.extension.loadRoundedImage
import ru.terrakok.gitlabclient.extension.shareText
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoPresenter
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
class ProjectInfoFragment : BaseFragment(), ProjectInfoView {

    override val layoutRes = R.layout.fragment_project_info
    private var project: Project? = null

    @InjectPresenter lateinit var presenter: ProjectInfoPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectInfoPresenter =
            Toothpick
                    .openScopes(DI.PROJECT_SCOPE)
                    .getInstance(ProjectInfoPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        toolbar.inflateMenu(R.menu.share_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.shareAction -> shareText(project?.webUrl)
            }
            true
        }
    }

    override fun showProjectInfo(project: Project) {
        this.project = project
        
        toolbar.title = project.name
        titleTextView.text = project.nameWithNamespace
        descriptionTextView.text = project.description
        avatarImageView.loadRoundedImage(project.avatarUrl, context)
        iconImageView.setImageResource(when (project.visibility) {
            Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
            Visibility.INTERNAL -> R.drawable.ic_security_white_24dp
            else -> R.drawable.ic_globe_18dp
        })

        starsTextView.text = project.starCount.toString()
        forksTextView.text = project.forksCount.toString()
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showReadmeFile(html: String) {
        webView.loadData(html, "text/html", "UTF-8")
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
package ru.terrakok.gitlabclient.ui.project.info

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_info.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.extension.loadRoundedImage
import ru.terrakok.gitlabclient.extension.visible
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

    @InjectPresenter lateinit var presenter: ProjectInfoPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectInfoPresenter =
            Toothpick
                    .openScopes(DI.PROJECT_SCOPE)
                    .getInstance(ProjectInfoPresenter::class.java)

    override fun showProject(project: Project, mdReadme: CharSequence) {
        titleTextView.text = project.nameWithNamespace
        descriptionTextView.text = project.description

        avatarImageView.loadRoundedImage(project.avatarUrl, context)
        iconImageView.setBackgroundResource(R.drawable.circle)
        iconImageView.setImageResource(when (project.visibility) {
            Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
            Visibility.INTERNAL -> R.drawable.ic_security_white_24dp
            else -> R.drawable.ic_globe_18dp
        })
        divider.visible(true)

        starsTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0)
        starsTextView.text = project.starCount.toString()
        forksTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fork, 0, 0, 0)
        forksTextView.text = project.forksCount.toString()

        Markwon.setText(readmeTextView, mdReadme)
    }

    override fun showProgress(show: Boolean) {
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
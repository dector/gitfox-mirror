package ru.terrakok.gitlabclient.ui.project.info

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_info.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.extension.getTintDrawable
import ru.terrakok.gitlabclient.extension.setStartDrawable
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.project.info.ProjectInfoPresenter
import ru.terrakok.gitlabclient.presentation.project.info.ProjectInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.view.custom.bindProject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
class ProjectInfoFragment : BaseFragment(), ProjectInfoView {

    override val layoutRes = R.layout.fragment_project_info

    @InjectPresenter
    lateinit var presenter: ProjectInfoPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectInfoPresenter =
        scope.getInstance(ProjectInfoPresenter::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            starsTextView.setStartDrawable(context.getTintDrawable(R.drawable.ic_star_black_24dp, R.color.colorPrimary))
            forksTextView.setStartDrawable(context.getTintDrawable(R.drawable.ic_fork, R.color.colorPrimary))
            issuesTextView.setStartDrawable(context.getTintDrawable(R.drawable.ic_issues_18dp, R.color.colorPrimary))
        }
    }

    override fun showProject(project: Project, mdReadme: CharSequence) {
        titleTextView.text = project.nameWithNamespace
        descriptionTextView.text = project.description
        starsTextView.text = project.starCount.toString()
        forksTextView.text = project.forksCount.toString()
        issuesTextView.text = project.openIssuesCount.toString()

        avatarImageView.bindProject(project, false)
        iconImageView.setBackgroundResource(R.drawable.circle)
        iconImageView.setImageResource(
            when (project.visibility) {
                Visibility.PRIVATE -> R.drawable.ic_lock_white_18dp
                Visibility.INTERNAL -> R.drawable.ic_security_white_24dp
                else -> R.drawable.ic_globe_18dp
            }
        )

        Markwon.setText(readmeTextView, mdReadme)
    }

    override fun showProgress(show: Boolean) {
        projectInfoLayout.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
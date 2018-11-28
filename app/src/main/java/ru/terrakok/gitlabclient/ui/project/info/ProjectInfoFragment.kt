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
import ru.terrakok.gitlabclient.extension.*
import ru.terrakok.gitlabclient.presentation.project.info.ProjectInfoPresenter
import ru.terrakok.gitlabclient.presentation.project.info.ProjectInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
class ProjectInfoFragment : BaseFragment(), ProjectInfoView {

    override val layoutRes = R.layout.fragment_project_info
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    @InjectPresenter
    lateinit var presenter: ProjectInfoPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectInfoPresenter =
        Toothpick
            .openScopes(scopeName)
            .getInstance(ProjectInfoPresenter::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            starsTextView.setStartDrawable(context.getTintDrawable(R.drawable.ic_star_black_24dp, R.color.colorPrimary))
            forksTextView.setStartDrawable(context.getTintDrawable(R.drawable.ic_fork, R.color.colorPrimary))
        }
    }

    override fun showProject(project: Project, mdReadme: CharSequence) {
        titleTextView.text = project.nameWithNamespace
        descriptionTextView.text = project.description
        starsTextView.text = project.starCount.toString()
        forksTextView.text = project.forksCount.toString()

        avatarImageView.loadRoundedImage(project.avatarUrl, context)
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

    companion object {
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(scope: String) =
            ProjectInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}
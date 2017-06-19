package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_info.*
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.common.Project
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoPresenter
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
class ProjectInfoFragment : BaseFragment(), ProjectInfoView {
    companion object {
        private const val ARG_PROJECT_ID = "prj_id"

        fun createNewInstance(projectId: Long) = ProjectInfoFragment().apply {
            arguments = Bundle().also { it.putLong(ARG_PROJECT_ID, projectId) }
        }
    }

    override val layoutRes = R.layout.fragment_project_info

    @InjectPresenter lateinit var presenter: ProjectInfoPresenter
    @ProvidePresenter
    fun providePresenter() = ProjectInfoPresenter(arguments.getLong(ARG_PROJECT_ID)).also {
        Toothpick.inject(it, App.APP_SCOPE)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showProjectInfo(project: Project) {
        toolbar.title = project.name
        starsCountTV.text = project.starCount.toString()
        forksCountTV.text = project.forksCount.toString()
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showReadmeFile(html: String) {
        markdownView.loadData(html, "text/html", "UTF-8")
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
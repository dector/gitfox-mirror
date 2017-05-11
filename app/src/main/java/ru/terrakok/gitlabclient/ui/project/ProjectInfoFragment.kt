package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import android.util.Base64
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_info.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoPresenter
import ru.terrakok.gitlabclient.presentation.project.ProjectInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

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
    fun providePresenter() = ProjectInfoPresenter(arguments.getLong(ARG_PROJECT_ID))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showProjectInfo(project: Project) {
        toolbar.title = project.name
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showReadmeFile(rawFile: String) {
        markdownView.loadMarkdown(String(Base64.decode(rawFile.toByteArray(), Base64.DEFAULT)))
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() = presenter.onBackPressed()
}
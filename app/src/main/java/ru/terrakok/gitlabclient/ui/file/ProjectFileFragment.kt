package ru.terrakok.gitlabclient.ui.file

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.file.ProjectFilePresenter
import ru.terrakok.gitlabclient.presentation.file.ProjectFileView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
class ProjectFileFragment : BaseFragment(), ProjectFileView {

    override val layoutRes = R.layout.fragment_project_file

    @InjectPresenter
    lateinit var presenter: ProjectFilePresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick
            .openScope(DI.PROJECT_FILE_FLOW_SCOPE)
            .getInstance(ProjectFilePresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }
}
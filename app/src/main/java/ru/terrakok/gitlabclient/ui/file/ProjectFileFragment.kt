package ru.terrakok.gitlabclient.ui.file

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_file.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.*
import ru.terrakok.gitlabclient.presentation.file.ProjectFilePresenter
import ru.terrakok.gitlabclient.presentation.file.ProjectFileView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.view.custom.codehighlight.CodeHighlightView
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.argument
import ru.terrakok.gitlabclient.util.visible
import toothpick.Scope
import toothpick.config.Module

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
class ProjectFileFragment : BaseFragment(), ProjectFileView {

    private val projectId by argument(ARG_PROJECT_ID, 0L)
    private val filePath by argument<String>(ARG_FILE_PATH, null)
    private val fileReference by argument<String>(ARG_FILE_REFERENCE, null)

    override val layoutRes = R.layout.fragment_project_file

    override val parentScopeName = DI.SERVER_SCOPE

    @InjectPresenter
    lateinit var presenter: ProjectFilePresenter

    @ProvidePresenter
    fun providePresenter(): ProjectFilePresenter = scope.getInstance(ProjectFilePresenter::class.java)

    override fun installModules(scope: Scope) {
        super.installModules(scope)
        scope.installModules(
            object : Module() {
                init {
                    bind(PrimitiveWrapper::class.java)
                        .withName(ProjectId::class.java)
                        .toInstance(PrimitiveWrapper(projectId))
                    bind(String::class.java)
                        .withName(FilePath::class.java)
                        .toInstance(filePath)
                    bind(String::class.java)
                        .withName(FileReference::class.java)
                        .toInstance(fileReference)
                }
            }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.addSystemTopPadding()
        projectFileCodeHighlightView.setOnCodeHighlightProgressLister(
            object : CodeHighlightView.OnCodeHighlightListener {
                override fun onCodeHighlightStarted() {
                    fullscreenProgressView.visible(true)
                }

                override fun onCodeHighlightFinished() {
                    fullscreenProgressView.visible(false)
                }
            }
        )
        emptyView.setRefreshListener { presenter.onFileReload() }
    }

    override fun onResume() {
        super.onResume()
        projectFileCodeHighlightView.onResume()
    }

    override fun onPause() {
        super.onPause()
        projectFileCodeHighlightView.onPause()
    }

    override fun onDestroyView() {
        projectFileCodeHighlightView.destroy()
        super.onDestroyView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun setRawFile(rawFile: String) {
        projectFileCodeHighlightView.highlightCode(rawFile)
    }

    override fun showEmptyView(show: Boolean) {
        if (show) {
            emptyView.showEmptyData()
            fullscreenProgressView.visible(false)
        } else {
            emptyView.hide()
            fullscreenProgressView.visible(true)
        }
    }

    companion object {
        private const val ARG_PROJECT_ID = "arg_project_id"
        private const val ARG_FILE_PATH = "arg_file_path"
        private const val ARG_FILE_REFERENCE = "arg_file_reference"

        fun create(projectId: Long, filePath: String, fileReference: String) =
            ProjectFileFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PROJECT_ID, projectId)
                    putString(ARG_FILE_PATH, filePath)
                    putString(ARG_FILE_REFERENCE, fileReference)
                }
            }
    }
}
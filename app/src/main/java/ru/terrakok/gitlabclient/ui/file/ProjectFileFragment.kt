package ru.terrakok.gitlabclient.ui.file

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_project_file.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.file.ProjectFilePresenter
import ru.terrakok.gitlabclient.presentation.file.ProjectFileView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.FilePath
import ru.terrakok.gitlabclient.toothpick.qualifier.FileReference
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.global.view.custom.codehighlight.CodeHighlightView
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

    override val scopeModuleInstaller = { scope: Scope ->
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

    @InjectPresenter
    lateinit var presenter: ProjectFilePresenter

    @ProvidePresenter
    fun providePresenter(): ProjectFilePresenter = scope.getInstance(ProjectFilePresenter::class.java)

    private var zeroViewHolder: ZeroViewHolder? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
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
        zeroViewHolder = ZeroViewHolder(zeroLayout) { presenter.onFileReload() }
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
            zeroViewHolder?.showEmptyData()
            fullscreenProgressView.visible(false)
        } else {
            zeroViewHolder?.hide()
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
package ru.terrakok.gitlabclient.model.interactor.project

import android.util.Base64
import com.commonsware.cwac.anddown.AndDown
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.04.17.
 */
class ProjectInteractor(private val projectRepository: ProjectRepository) {
    val mdConverter = AndDown()

    fun getProject(id: Long) = projectRepository.getProject(id)

    fun getProjectReadmeHtml(id: Long, branchName: String) =
            projectRepository.getFile(id, "README.md", branchName)
                    .observeOn(Schedulers.computation())
                    .map { file ->
                        val md = String(Base64.decode(file.content.toByteArray(), Base64.DEFAULT))
                        val html = mdConverter.markdownToHtml(md)
                        html
                    }
                    .observeOn(AndroidSchedulers.mainThread())
}
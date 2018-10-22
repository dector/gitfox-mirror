package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.toothpick.provider.MarkDownConverterProvider
import javax.inject.Inject

class ProjectMarkDownConverterProvider @Inject constructor(
    private val markdownConverterProvider: MarkDownConverterProvider,
    private val projectInteractor: ProjectInteractor
) {

    fun getMarkdownConverter(projectId: Long?): Single<MarkDownConverter> {
        if (projectId != null) {
            return projectInteractor
                .getProjectLabels(projectId)
                .map { markdownConverterProvider.get(it) }
        } else {
            return Single.just(markdownConverterProvider.get(emptyList()))
        }
    }

}
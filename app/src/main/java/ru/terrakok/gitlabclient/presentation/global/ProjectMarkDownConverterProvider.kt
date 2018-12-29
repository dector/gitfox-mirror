package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.Single
import ru.terrakok.gitlabclient.model.interactor.label.LabelInteractor
import ru.terrakok.gitlabclient.toothpick.provider.MarkDownConverterProvider
import javax.inject.Inject

class ProjectMarkDownConverterProvider @Inject constructor(
    private val markdownConverterProvider: MarkDownConverterProvider,
    private val projectInteractor: LabelInteractor
) {

    fun getMarkdownConverter(projectId: Long?): Single<MarkDownConverter> {
        if (projectId != null) {
            return projectInteractor
                .getAllProjectLabels(projectId)
                .map { markdownConverterProvider.get(it) }
        } else {
            return Single.just(markdownConverterProvider.get(emptyList()))
        }
    }

}
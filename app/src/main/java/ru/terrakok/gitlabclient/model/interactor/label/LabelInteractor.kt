package ru.terrakok.gitlabclient.model.interactor.label

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.model.data.cache.ProjectLabelCache
import ru.terrakok.gitlabclient.model.repository.label.LabelRepository
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor @Inject constructor(
    private val labelRepository: LabelRepository,
    private val projectLabelCache: ProjectLabelCache
) {

    fun getLabelList(
        projectId: Long,
        page: Int
    ) = labelRepository.getLabelList(projectId, page)

    fun getAllProjectLabels(
        projectId: Long
    ): Single<List<Label>> {
        return Single.defer {
            val labels = projectLabelCache.get(projectId)
            if (labels != null) {
                Single.just(labels)
            } else {
                getAllProjectLabels(projectId, 0)
                    .doOnSuccess { projectLabelCache.put(projectId, it) }
            }
        }
    }

    private fun getAllProjectLabels(projectId: Long, currentPage: Int): Single<List<Label>> {
        return labelRepository
            .getLabelList(projectId, currentPage)
            .flatMap {labels ->
                if (labels.isEmpty()) {
                    Single.just(labels)
                } else {
                    getAllProjectLabels(projectId, currentPage + 1)
                        .map { nextLabels -> labels + nextLabels }
                }
            }
    }

    fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ) = labelRepository.subscribeToLabel(projectId, labelId)

    fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ) = labelRepository.unsubscribeFromLabel(projectId, labelId)

}
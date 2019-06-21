package ru.terrakok.gitlabclient.model.interactor.label

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.model.repository.label.LabelRepository
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor @Inject constructor(
    private val labelRepository: LabelRepository
) {

    fun getLabelList(
        projectId: Long,
        page: Int
    ) = labelRepository.getLabelList(projectId, page)

    fun getAllProjectLabels(
        projectId: Long
    ): Single<List<Label>> = labelRepository.getAllProjectLabels(projectId)

    fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ) = labelRepository.subscribeToLabel(projectId, labelId)

    fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ) = labelRepository.unsubscribeFromLabel(projectId, labelId)

}
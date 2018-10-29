package ru.terrakok.gitlabclient.model.interactor.label

import ru.terrakok.gitlabclient.model.repository.label.LabelRepository
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor @Inject constructor(
    private val labelRepository: LabelRepository
) {

    fun getLabelList(projectId: Long) = labelRepository.getLabelList(projectId)

}
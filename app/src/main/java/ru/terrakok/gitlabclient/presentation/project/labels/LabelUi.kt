package ru.terrakok.gitlabclient.presentation.project.labels

import ru.terrakok.gitlabclient.entity.Label

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
data class LabelUi(
    val origin: Label,
    val isLoading: Boolean
)
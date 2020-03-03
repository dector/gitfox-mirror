package ru.terrakok.gitlabclient.model.system.message

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
data class SystemMessage(
    val text: String,
    val type: SystemMessageType = SystemMessageType.ALERT
)

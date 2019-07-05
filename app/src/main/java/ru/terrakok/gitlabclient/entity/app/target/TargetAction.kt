package ru.terrakok.gitlabclient.entity.app.target

import java.io.Serializable

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.05.2019.
 */
sealed class TargetAction : Serializable {

    data class CommentedOn(val noteId: Long) : TargetAction()

    object Undefined : TargetAction()
}
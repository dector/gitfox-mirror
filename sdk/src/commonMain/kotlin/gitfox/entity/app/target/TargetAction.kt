package gitfox.entity.app.target

/**
 * @author Maxim Myalkin (MaxMyalkin) on 19.05.2019.
 */
sealed class TargetAction {

    data class CommentedOn(val noteId: Long) : TargetAction()

    object Undefined : TargetAction()
}

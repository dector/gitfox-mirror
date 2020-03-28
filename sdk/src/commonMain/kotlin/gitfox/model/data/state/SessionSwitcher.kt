package gitfox.model.data.state

import gitfox.entity.app.session.UserAccount

internal interface SessionSwitcher {
    val hasSession: Boolean
    fun initSession(newAccount: UserAccount?)
}
package gitfox.model.data.state

import gitfox.entity.app.session.UserAccount

interface SessionSwitcher {
    val hasSession: Boolean
    fun initSession(newAccount: UserAccount?)
}
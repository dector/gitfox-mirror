package gitfox.entity.app.session

data class AuthHolder(
    val token: String?,
    val isOAuth: Boolean
)

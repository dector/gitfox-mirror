package gitfox.model.data.storage

import com.russhwolf.settings.Settings
import gitfox.entity.app.session.UserAccount
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
internal class Prefs(
    private val settings: Settings,
    private val json: Json
) {
    private val KEY_CURRENT_ACCOUNT = "ad_current_account"
    private val KEY_USER_ACCOUNTS = "ad_accounts"
    private val KEY_FIRST_LAUNCH_TIME = "launch_ts"
    private val KEY_OAUTH_HASH = "oauth_hash"

    var selectedAccount: String?
        get() = settings.getStringOrNull(KEY_CURRENT_ACCOUNT)
        set(value) {
            value?.let {
                settings.putString(KEY_CURRENT_ACCOUNT, value)
            } ?: run {
                settings.remove(KEY_CURRENT_ACCOUNT)
            }
        }

    var accounts: List<UserAccount>
        get() = json.decodeFromString(
            ListSerializer(UserAccount.serializer()),
            settings.getString(KEY_USER_ACCOUNTS, "[]")
        )
        set(value) {
            settings.putString(
                KEY_USER_ACCOUNTS,
                json.encodeToString(ListSerializer(UserAccount.serializer()), value)
            )
        }

    fun getCurrentUserAccount(): UserAccount? {
        selectedAccount?.let { id ->
            return accounts.find { it.id == id }
        }
        return null
    }

    var firstLaunchTimeStamp: Long?
        get() = settings.getLong(KEY_FIRST_LAUNCH_TIME, 0).takeIf { it > 0 }
        set(value) {
            settings.putLong(KEY_FIRST_LAUNCH_TIME, value ?: 0)
        }

    var oauthHash: String
        get() = settings.getString(KEY_OAUTH_HASH)
        set(value) {
            settings.putString(KEY_OAUTH_HASH, value)
        }
}

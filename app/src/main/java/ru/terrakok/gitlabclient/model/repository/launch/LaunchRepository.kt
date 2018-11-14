package ru.terrakok.gitlabclient.model.repository.launch

import ru.terrakok.gitlabclient.model.data.storage.Prefs
import javax.inject.Inject

class LaunchRepository @Inject constructor(
    private val prefs: Prefs
) {

    val isSignedIn get() = prefs.token != null

    fun getSelectedServerPath() = prefs.serverPath
}
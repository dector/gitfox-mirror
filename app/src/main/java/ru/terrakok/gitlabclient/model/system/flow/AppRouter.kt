package ru.terrakok.gitlabclient.model.system.flow

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Forward

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 27.09.18.
 */
open class AppRouter : Router() {
    fun navigateTo(vararg screens: Pair<String, Any?>) {
        executeCommands(
            *screens
                .map { (screenKey, data) -> Forward(screenKey, data) }
                .toTypedArray()
        )
    }

    fun newRootScreens(vararg screens: Pair<String, Any?>) {
        val commands =
            listOf(BackTo(null))
                .plus(screens.map { (screenKey, data) -> Forward(screenKey, data) })
        executeCommands(*commands.toTypedArray())
    }
}
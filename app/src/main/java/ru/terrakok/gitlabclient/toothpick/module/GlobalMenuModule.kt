package ru.terrakok.gitlabclient.toothpick.module

import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.07.17.
 */
class GlobalMenuModule : Module() {
    init {
        bind(GlobalMenuController::class.java).toInstance(GlobalMenuController())
    }
}
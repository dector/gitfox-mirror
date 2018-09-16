package ru.terrakok.gitlabclient.model.system.flow

import ru.terrakok.cicerone.Router

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 19.12.17.
 */
class FlowRouter(private val appRouter: Router) : Router() {

    fun startFlow(screenKey: String, data: Any? = null) {
        appRouter.navigateTo(screenKey, data)
    }

    fun newRootFlow(screenKey: String, data: Any? = null) {
        appRouter.newRootScreen(screenKey, data)
    }

    fun finishFlow() {
        appRouter.exit()
    }
}
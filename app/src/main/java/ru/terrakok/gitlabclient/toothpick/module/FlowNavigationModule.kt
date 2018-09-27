package ru.terrakok.gitlabclient.toothpick.module

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.gitlabclient.model.system.flow.AppRouter
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class FlowNavigationModule(globalRouter: AppRouter) : Module() {
    init {
        val cicerone = Cicerone.create(FlowRouter(globalRouter))
        bind(FlowRouter::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}
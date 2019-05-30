package ru.terrakok.gitlabclient.di.module

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import toothpick.config.Module

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class FlowNavigationModule(globalRouter: Router) : Module() {
    init {
        val cicerone = Cicerone.create(FlowRouter(globalRouter))
        bind(FlowRouter::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    }
}
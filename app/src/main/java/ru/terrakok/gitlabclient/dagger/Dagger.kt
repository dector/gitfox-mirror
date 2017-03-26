package ru.terrakok.gitlabclient.dagger

import ru.terrakok.gitlabclient.dagger.component.DaggerAppComponent

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class Dagger {
    val appComponent = DaggerAppComponent.builder().build()
}
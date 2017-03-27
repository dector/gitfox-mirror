package ru.terrakok.gitlabclient.dagger

import android.content.Context
import ru.terrakok.gitlabclient.dagger.component.DaggerAppComponent
import ru.terrakok.gitlabclient.dagger.module.AppModule

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class Dagger(private val context: Context) {
    val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context))
            .build()
}
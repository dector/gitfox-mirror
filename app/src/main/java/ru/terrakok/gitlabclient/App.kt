package ru.terrakok.gitlabclient

import android.app.Application
import android.content.Context
import ru.terrakok.gitlabclient.toothpick.module.AppModule
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {
    companion object {
        lateinit var APP_SCOPE: Scope
    }

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initAppScope(this)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppScope(context: Context) {
        APP_SCOPE = Toothpick.openScope(this)
        APP_SCOPE.installModules(AppModule(context))
    }
}
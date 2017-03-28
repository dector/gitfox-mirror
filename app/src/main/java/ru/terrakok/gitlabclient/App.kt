package ru.terrakok.gitlabclient

import android.app.Application
import ru.terrakok.gitlabclient.dagger.Dagger
import timber.log.Timber

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {
    companion object {
        lateinit var DAGGER: Dagger
    }

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initDagger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun initDagger() {
        DAGGER = Dagger(this)
    }
}
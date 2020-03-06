package ru.terrakok.gitlabclient

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.module.AppModule
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            MultiDex.install(this)
        }
    }

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initToothpick()
        initAppScope()
        initThreetenABP()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initAppScope() {
        Toothpick.openScope(DI.APP_SCOPE)
            .installModules(AppModule(this))
    }

    private fun initThreetenABP() {
        AndroidThreeTen.init(this)
    }
}

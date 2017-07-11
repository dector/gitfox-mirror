package ru.terrakok.gitlabclient

import android.app.Application
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.AppModule
import timber.log.Timber
import toothpick.Toothpick
import uk.co.chrisjenx.calligraphy.CalligraphyConfig



/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initAppScope()
        initCalligraphy()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initAppScope() {
        Toothpick.openScope(DI.APP_SCOPE).installModules(AppModule(this))
    }

    private fun initCalligraphy() {
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.font_main_regular))
                .setFontAttrId(R.attr.fontPath)
                .build())
    }
}
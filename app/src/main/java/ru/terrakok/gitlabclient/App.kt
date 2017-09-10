package ru.terrakok.gitlabclient

import android.app.Application
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.AppModule
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initToothpick()
        initAppScope()
        initCalligraphy()
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
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(ru.terrakok.gitlabclient.FactoryRegistry())
            MemberInjectorRegistryLocator.setRootRegistry(ru.terrakok.gitlabclient.MemberInjectorRegistry())
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
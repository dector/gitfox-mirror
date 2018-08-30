package ru.terrakok.gitlabclient

import android.app.Application
import android.graphics.Color
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.spans.SpannableTheme
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.AppModule
import ru.terrakok.gitlabclient.toothpick.module.ServerModule
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistryLocator

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initToothpick()
        initAppScope()
        initMarkwon()
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
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
            FactoryRegistryLocator.setRootRegistry(ru.terrakok.gitlabclient.FactoryRegistry())
            MemberInjectorRegistryLocator.setRootRegistry(ru.terrakok.gitlabclient.MemberInjectorRegistry())
        }
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(AppModule(this))

        //By default we need init ServerScope for launch app
        val authHolder = appScope.getInstance(AuthHolder::class.java)
        val serverScope = Toothpick.openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
        serverScope.installModules(ServerModule(authHolder.serverPath))
    }

    private fun initMarkwon() {
        val theme = SpannableTheme.builderWithDefaults(this)
                .codeTextColor(Color.parseColor("#C0341D"))
                .codeBackgroundColor(Color.parseColor("#FCEDEA"))
                .build()
        SpannableConfiguration.builder(this)
                .theme(theme)
                .build()
    }

    private fun initThreetenABP() {
        AndroidThreeTen.init(this)
    }
}
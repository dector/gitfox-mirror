package ru.terrakok.gitlabclient

import android.app.Application
import android.graphics.Color
import com.crashlytics.android.Crashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import io.fabric.sdk.android.Fabric
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.spans.SpannableTheme
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.module.AppModule
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import java.util.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appCode = UUID.randomUUID().toString()

        initLogger()
        initFabric()
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

    private fun initFabric() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(
                Fabric.Builder(this)
                    .kits(Crashlytics())
                    .build()
            )
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

    companion object {
        lateinit var appCode: String
            private set
    }
}
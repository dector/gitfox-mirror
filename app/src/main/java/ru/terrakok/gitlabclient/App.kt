package ru.terrakok.gitlabclient

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tspoon.traceur.Traceur
import ru.noties.markwon.SpannableConfiguration
import ru.noties.markwon.spans.SpannableTheme
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
        initMarkwon()
        initThreetenABP()
        maybeInitTraceur()
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

    private fun maybeInitTraceur() {
        Traceur.enableLogging()
    }
}

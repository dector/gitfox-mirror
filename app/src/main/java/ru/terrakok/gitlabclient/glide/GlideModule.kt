package ru.terrakok.gitlabclient.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import toothpick.Toothpick
import java.io.InputStream
import javax.inject.Inject

/**
 * Created by Alexei Korshun on 25/10/2018.
 */
@GlideModule
class GlideModule : AppGlideModule() {

    @Inject lateinit var authHolder: AuthHolder
    @Inject lateinit var okHttpClientFactory: OkHttpClientFactory

    init {
        Toothpick.inject(this, Toothpick.openScope(DI.SERVER_SCOPE))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val factory = OkHttpUrlLoader.Factory(
            okHttpClientFactory.create(authHolder, false, BuildConfig.DEBUG)
        )

        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    override fun isManifestParsingEnabled() = false
}
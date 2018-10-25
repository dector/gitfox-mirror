package ru.terrakok.gitlabclient.toothpick

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import toothpick.Toothpick
import java.io.InputStream

/**
 * Created by Alexei Korshun on 25/10/2018.
 */
@GlideModule
class GlideModule : AppGlideModule() {

    private val okHttpClient: OkHttpClient = Toothpick
        .openScope(DI.SERVER_SCOPE)
        .getInstance(OkHttpClient::class.java)

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val factory = OkHttpUrlLoader.Factory(okHttpClient)

        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }
}
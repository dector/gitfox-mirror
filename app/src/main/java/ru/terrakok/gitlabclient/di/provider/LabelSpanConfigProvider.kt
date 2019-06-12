package ru.terrakok.gitlabclient.di.provider

import android.content.Context
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.markwonx.label.LabelSpanConfig
import javax.inject.Inject
import javax.inject.Provider

class LabelSpanConfigProvider @Inject constructor(
    val context: Context
) : Provider<LabelSpanConfig> {

    override fun get(): LabelSpanConfig = LabelSpanConfig(
        padding = context.resources.getDimensionPixelSize(R.dimen.label_padding)
    )

}
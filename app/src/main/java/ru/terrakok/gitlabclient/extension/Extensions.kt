package ru.terrakok.gitlabclient.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.AppTarget
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import timber.log.Timber

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 03.03.17
 */
fun Context.color(colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun ImageView.tint(colorRes: Int) = this.setColorFilter(this.context.color(colorRes))

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextView.showTextOrHide(str: String?) {
    this.text = str
    this.visible(!str.isNullOrBlank())
}

fun Fragment.tryOpenLink(link: String?, basePath: String? = "https://google.com/search?q=") {
    if (link != null) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    when {
                        URLUtil.isValidUrl(link) -> Uri.parse(link)
                        else -> Uri.parse(basePath + link)
                    }
                )
            )
        } catch (e: Exception) {
            Timber.e("tryOpenLink error: $e")
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://google.com/search?q=$link")
                )
            )
        }
    }
}

fun Fragment.shareText(text: String?) {
    text?.let {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                },
                getString(R.string.share_to)
            )
        )
    }
}

fun Fragment.sendEmail(email: String?) {
    if (email != null) {
        startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null)),
                null
            )
        )
    }
}

fun ImageView.loadRoundedImage(
    url: String?,
    ctx: Context? = null
) {
    Glide.with(ctx ?: context)
        .load(url)
        .apply(RequestOptions().apply {
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        })
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}

fun TargetHeader.openInfo(router: FlowRouter) {
    when (target) {
        AppTarget.PROJECT -> {
            router.startFlow(Screens.PROJECT_FLOW, targetId)
        }
        AppTarget.USER -> {
            router.startFlow(Screens.USER_FLOW, targetId)
        }
        AppTarget.MERGE_REQUEST -> {
            internal?.let { targetInternal ->
                router.startFlow(
                    Screens.MR_FLOW,
                    Pair(targetInternal.projectId, targetInternal.targetIid)
                )
            }
        }
        AppTarget.ISSUE -> {
            internal?.let { targetInternal ->
                router.startFlow(
                    Screens.ISSUE_FLOW,
                    Pair(targetInternal.projectId, targetInternal.targetIid)
                )
            }
        }
        else -> {
            internal?.let { targetInternal ->
                Timber.i("Temporary open project flow")
                //todo
                router.startFlow(Screens.PROJECT_FLOW, targetInternal.projectId)
            }
        }
    }
}
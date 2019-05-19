package ru.terrakok.gitlabclient.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Replace
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.AppTarget
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderTitle
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import timber.log.Timber

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 03.03.17
 */
fun Navigator.setLaunchScreen(screen: SupportAppScreen) {
    applyCommands(
        arrayOf(
            BackTo(null),
            Replace(screen)
        )
    )
}

fun Context.color(colorRes: Int) = ContextCompat.getColor(this, colorRes)

fun Context.getTintDrawable(drawableRes: Int, colorRes: Int): Drawable {
    val source = ContextCompat.getDrawable(this, drawableRes)!!.mutate()
    val wrapped = DrawableCompat.wrap(source)
    DrawableCompat.setTint(wrapped, color(colorRes))
    return wrapped
}

fun Context.getTintDrawable(drawableRes: Int, colorResources: IntArray, states: Array<IntArray>): Drawable {
    val source = ContextCompat.getDrawable(this, drawableRes)!!.mutate()
    val wrapped = DrawableCompat.wrap(source)
    DrawableCompat.setTintList(wrapped, ColorStateList(states, colorResources.map { color(it) }.toIntArray()))
    return wrapped
}

fun TextView.setStartDrawable(drawable: Drawable) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        drawable,
        null,
        null,
        null
    )
}

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

fun TargetHeader.Public.openInfo(router: FlowRouter) {
    when (target) {
        AppTarget.PROJECT -> {
            router.startFlow(Screens.ProjectFlow(targetId))
        }
        AppTarget.USER -> {
            router.startFlow(Screens.UserFlow(targetId))
        }
        AppTarget.MERGE_REQUEST -> {
            val eventAction = (title as? TargetHeaderTitle.Event)?.action
            internal?.let { targetInternal ->
                router.startFlow(
                    Screens.MergeRequestFlow(
                        targetInternal.projectId,
                        targetInternal.targetIid,
                        eventAction
                    )
                )
            }
        }
        AppTarget.ISSUE -> {
            internal?.let { targetInternal ->
                router.startFlow(Screens.IssueFlow(targetInternal.projectId, targetInternal.targetIid))
            }
        }
        else -> {
            internal?.let { targetInternal ->
                Timber.i("Temporary open project flow")
                //todo
                router.startFlow(Screens.ProjectFlow(targetInternal.projectId))
            }
        }
    }
}

fun Fragment.showSnackMessage(message: String) {
    view?.let {
        val ssb = SpannableStringBuilder().apply {
            append(message)
            setSpan(
                ForegroundColorSpan(Color.WHITE),
                0,
                message.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        Snackbar.make(it, ssb, Snackbar.LENGTH_LONG).show()
    }
}

fun Activity.hideKeyboard() {
    currentFocus?.apply {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Any.objectScopeName() = "${javaClass.simpleName}_${hashCode()}"

fun View.setBackgroundTintByColor(@ColorInt color: Int) {
    val wrappedDrawable = DrawableCompat.wrap(background)
    DrawableCompat.setTint(wrappedDrawable.mutate(), color)
}
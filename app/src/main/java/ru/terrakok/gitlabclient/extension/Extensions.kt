package ru.terrakok.gitlabclient.extension

import android.content.res.Resources
import android.os.Build
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.model.system.ResourceManager
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 03.03.17
 */
fun Resources.color(colorRes: Int) =
        if (Build.VERSION.SDK_INT >= 23) {
            this.getColor(colorRes, null)
        } else {
            this.getColor(colorRes)
        }

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun Throwable.userMessage(resourceManager: ResourceManager) = when (this) {
    is HttpException -> when (this.code()) {
        304 -> resourceManager.getString(R.string.not_modified_error)
        400 -> resourceManager.getString(R.string.bad_request_error)
        401 -> resourceManager.getString(R.string.unauthorized_error)
        403 -> resourceManager.getString(R.string.forbidden_error)
        404 -> resourceManager.getString(R.string.not_found_error)
        405 -> resourceManager.getString(R.string.method_not_allowed_error)
        409 -> resourceManager.getString(R.string.conflict_error)
        422 -> resourceManager.getString(R.string.unprocessable_error)
        500 -> resourceManager.getString(R.string.server_error_error)
        else -> resourceManager.getString(R.string.unknown_error)
    }
    is IOException -> resourceManager.getString(R.string.network_error)
    else -> resourceManager.getString(R.string.unknown_error)
}

private val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
fun Date.humanTime(resources: Resources): String {
    val timeDelta = (System.currentTimeMillis() - this.time) / 1000L
    val timeStr =
            if (timeDelta < 60) {
                resources.getString(R.string.time_sec, timeDelta)
            } else if (timeDelta < 60*60) {
                resources.getString(R.string.time_min, timeDelta)
            } else if (timeDelta < 60*60*24) {
                resources.getString(R.string.time_hour, timeDelta)
            }  else if (timeDelta < 60*60*24*7) {
                resources.getString(R.string.time_day, timeDelta)
            } else {
                return DATE_FORMAT.format(this)
            }

    return resources.getString(R.string.time_ago, timeStr)
}

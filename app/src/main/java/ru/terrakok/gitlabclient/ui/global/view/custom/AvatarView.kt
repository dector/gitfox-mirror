package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.view_avatar.view.*
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.extension.getTintDrawable
import ru.terrakok.gitlabclient.extension.visible
import toothpick.Toothpick

class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val shortNameBackgroundColors = intArrayOf(
        R.color.green,
        R.color.silver,
        R.color.grey,
        R.color.fruit_salad,
        R.color.red,
        R.color.brandy_punch,
        R.color.blue,
        R.color.mariner
    )

    init {
        View.inflate(context, R.layout.view_avatar, this)
    }

    fun setData(id: Long, name: String, avatarUrl: String?, clickAction: () -> Unit = {}) {
        Glide.with(avatarImage)
            .clear(avatarImage)

        val names = name.split(" ")
        val shortName = when (names.size) {
            0 -> ""
            1 -> getShortNameFromSingleWord(names[0])
            else -> getShortNameFromMultipleWords(names)
        }
        if (shortName.length == SHORT_NAME_LENGTH) {
            avatarShortName.text = shortName
            val shortNameBackgroundColor = shortNameBackgroundColors[(id % shortNameBackgroundColors.size).toInt()]
            avatarShortName.background = context.getTintDrawable(R.drawable.circle, shortNameBackgroundColor)
            avatarShortName.visible(true)
        } else {
            avatarImage.setImageResource(R.drawable.default_img)
            avatarShortName.visible(false)
        }

        Glide.with(avatarImage)
            .load(avatarUrl)
            .listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        avatarShortName.visible(false)
                        return false
                    }
                }
            )
            .apply(RequestOptions.circleCropTransform())
            .into(avatarImage)

        setOnClickListener { clickAction() }
    }

    private fun getShortNameFromSingleWord(name: String): String {
        val result = StringBuilder()
        name.forEach {
            if (it.isLetter()) {
                result.append(it.toUpperCase())
            }
            if (result.length == SHORT_NAME_LENGTH) {
                return result.toString()
            }
        }
        return result.toString()
    }

    private fun getShortNameFromMultipleWords(names: List<String>): String {
        val result = StringBuilder()
        names.forEach {
            val letter = it.find { char -> char.isLetter() }
            if (letter != null) {
                result.append(letter.toUpperCase())
            }
            if (result.length == SHORT_NAME_LENGTH) {
                return result.toString()
            }
        }
        return result.toString()
    }

    companion object {
        private const val SHORT_NAME_LENGTH = 2
    }
}

fun AvatarView.bindShortUser(shortUser: ShortUser, withNavigation: Boolean = true) {
    with(shortUser) {
        if (withNavigation) {
            val router = Toothpick.openScope(DI.APP_SCOPE).getInstance(Router::class.java)
            setData(id, name, avatarUrl) { router.navigateTo(Screens.UserFlow(id)) }
        } else {
            setData(id, name, avatarUrl)
        }
    }
}

fun AvatarView.bindUserAccount(userAccount: UserAccount, withNavigation: Boolean) {
    with(userAccount) {
        if (withNavigation) {
            val router = Toothpick.openScope(DI.APP_SCOPE).getInstance(Router::class.java)
            setData(userId, userName, avatarUrl) { router.navigateTo(Screens.UserFlow(userId)) }
        } else {
            setData(userId, userName, avatarUrl)
        }
    }
}

fun AvatarView.bindProject(project: Project, withNavigation: Boolean = true) {
    with(project) {
        if (withNavigation) {
            val router = Toothpick.openScope(DI.APP_SCOPE).getInstance(Router::class.java)
            setData(id, name, avatarUrl) { router.navigateTo(Screens.ProjectFlow(id)) }
        } else {
            setData(id, name, avatarUrl)
        }
    }
}
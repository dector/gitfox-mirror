package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
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

    private val nameBackgroundColors = intArrayOf(
        R.color.green,
        R.color.silver,
        R.color.grey,
        R.color.fruit_salad,
        R.color.red,
        R.color.brandy_punch,
        R.color.blue,
        R.color.mariner
    )

    private var nameBackgroundColor = R.color.green
    private var isTextSizeSet = false

    init {
        View.inflate(context, R.layout.view_avatar, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (!isTextSizeSet && measuredWidth > 0) {
            val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48f, resources.displayMetrics).toInt()
            val textSize = when {
                measuredWidth < width -> 16f
                measuredWidth == width -> 18f
                else -> 22f
            }
            avatarName.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            isTextSizeSet = true
        }
    }

    fun setData(id: Long, name: String, avatarUrl: String?, clickAction: () -> Unit = {}) {
        nameBackgroundColor = nameBackgroundColors[(id % nameBackgroundColors.size).toInt()]

        Glide.with(avatarImage)
            .clear(avatarImage)

        Glide.with(avatarImage)
            .load(avatarUrl)
            .apply(RequestOptions.placeholderOf(R.drawable.default_img))
            .listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        handleLoadFailed(name)
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                }
            )
            .apply(RequestOptions.circleCropTransform())
            .into(avatarImage)

        setOnClickListener { clickAction() }
    }

    private fun handleLoadFailed(name: String) {
        val names = name.split(" ")
        val shortName = when (names.size) {
            0 -> ""
            1 -> getShortNameFromSingleWord(names[0])
            else -> getShortNameFromMultipleWords(names)
        }

        if (shortName.length == SHORT_NAME_LENGTH) {
            avatarName.text = shortName
            avatarName.background = context.getTintDrawable(R.drawable.circle, nameBackgroundColor)
            avatarImage.visible(false)
            avatarName.visible(true)
        } else {
            avatarImage.setImageResource(R.drawable.default_img)
            avatarImage.visible(true)
            avatarName.visible(false)
        }
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

fun AvatarView.bindShortUser(shortUser: ShortUser, withNavigation: Boolean) {
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

fun AvatarView.bindProject(project: Project) {
    with(project) {
        setData(id, name, avatarUrl, {})
    }
}
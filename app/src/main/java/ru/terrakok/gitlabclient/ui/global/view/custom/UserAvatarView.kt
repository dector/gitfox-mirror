package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.DI
import toothpick.Toothpick

class UserAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    private val router = Toothpick.openScope(DI.APP_SCOPE).getInstance(Router::class.java)

    private var userId: Long? = null

    init {
        setOnClickListener {
            userId?.let {
                router.navigateTo(Screens.UserFlow(it))
            }
        }
    }

    fun setUserInfo(userId: Long, avatarUrl: String?) {
        this.userId = userId
        Glide.with(context)
            .load(avatarUrl)
            .apply(
                RequestOptions().apply {
                    placeholder(R.drawable.default_img)
                    error(R.drawable.default_img)
                }
            )
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    }
}
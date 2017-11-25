package ru.terrakok.gitlabclient.ui.global.holder

import android.graphics.Bitmap
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.layout_avatar.view.*

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class AvatarViewHolder(private val view: ViewGroup) {

    fun setData(avatarUrl: String?, name: String?) {
        view.letterTV.text = name?.first()?.toString()?.toUpperCase()
        Glide.with(view.avatarIV.context)
                .load(avatarUrl)
                .asBitmap()
                .centerCrop()
                .into(object : BitmapImageViewTarget(view.avatarIV) {
                    override fun setResource(resource: Bitmap?) {
                        resource?.let {
                            view.avatarIV.visibility = View.VISIBLE
                            RoundedBitmapDrawableFactory.create(view.resources, it).run {
                                this.isCircular = true
                                view.avatarIV.setImageDrawable(this)
                            }
                        }
                    }
                })
    }
}
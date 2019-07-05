package ru.terrakok.gitlabclient.ui.global.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_member.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.extension.inflate
import ru.terrakok.gitlabclient.ui.global.view.custom.bindMember

/**
 * @author Valentin Logvinovitch (glvvl) on 28.02.19.
 */

fun Member.isSame(other: Member) = id == other.id

class MembersAdapterDelegate(
    private val clickListener: (Long) -> Unit
) : AdapterDelegate<MutableList<Any>>() {

    override fun isForViewType(items: MutableList<Any>, position: Int) =
        items[position] is Member

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_member))

    override fun onBindViewHolder(
        items: MutableList<Any>,
        position: Int,
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) = (viewHolder as ViewHolder).bind(items[position] as Member)

    private inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val guestTitle = containerView.context.getString(R.string.members_guest)
        private val reporterTitle = containerView.context.getString(R.string.members_reporter)
        private val developerTitle = containerView.context.getString(R.string.members_developer)
        private val maintainerTitle = containerView.context.getString(R.string.members_maintainer)
        private val ownerTitle = containerView.context.getString(R.string.members_owner)

        private lateinit var data: Member

        init {
            containerView.setOnClickListener { clickListener(data.id) }
        }

        fun bind(data: Member) {
            this.data = data
            avatarView.bindMember(data)
            nameTextView.text = data.name
            roleTextView.text = data.accessLevel.accessToString()
        }

        private fun Long.accessToString(): String =
            when (this) {
                10L -> guestTitle
                20L -> reporterTitle
                30L -> developerTitle
                40L -> maintainerTitle
                50L -> ownerTitle
                else -> throw IllegalArgumentException("You must provide correct value to accessLevel")
            }
    }
}
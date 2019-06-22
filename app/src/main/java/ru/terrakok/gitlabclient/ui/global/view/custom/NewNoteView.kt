package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_new_note.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.getTintDrawable

class NewNoteView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_new_note, this)
    }

    fun init(onSendClickListener: (String) -> Unit) {
        newNoteEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                newNoteSend.isEnabled = s.isNotEmpty()
            }
        })
        newNoteSend.setImageDrawable(
            context.getTintDrawable(
                R.drawable.ic_send_24dp,
                intArrayOf(R.color.grey_30, R.color.grey),
                arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled))
            )
        )
        newNoteSend.setOnClickListener { onSendClickListener.invoke(newNoteEditText.text.toString().trim()) }
        newNoteSend.isEnabled = newNoteEditText.text.isNotEmpty()
    }

    fun clearInput() = newNoteEditText.setText("")
}
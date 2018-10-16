package ru.terrakok.gitlabclient.ui.global

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.layout_note_input.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.getTintDrawable

class NoteInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var onSendClickListener: (String) -> Unit

    init {
        View.inflate(context, R.layout.layout_note_input, this)

        noteInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                noteSend.isEnabled = s.isNotEmpty()
            }
        })
        noteSend.setImageDrawable(
            context.getTintDrawable(
                R.drawable.ic_send_24dp,
                intArrayOf(R.color.grey_30, R.color.grey),
                arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_enabled))
            )
        )
        noteSend.setOnClickListener { onSendClickListener.invoke(noteInput.text.toString().trim()) }
        noteSend.isEnabled = noteInput.text.isNotEmpty()
    }

    fun setOnSendClickListener(clickListener: (String) -> Unit) {
        onSendClickListener = clickListener
    }

    fun clearInput() = noteInput.setText("")
}
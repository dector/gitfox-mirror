package ru.terrakok.gitlabclient.ui.global

import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.getTintDrawable

class NewNoteViewController(
    root: ViewGroup,
    onSendClickListener: (String) -> Unit
) {

    private val newNoteEditText = root.findViewById<EditText>(R.id.newNoteEditText)
    private val newNoteSend = root.findViewById<ImageView>(R.id.newNoteSend)

    init {
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
            root.context.getTintDrawable(
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
package ru.terrakok.gitlabclient.ui.global

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import ru.terrakok.gitlabclient.R


class ConfirmDialog : DialogFragment() {
    private var clickListener: OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle).apply {
                arguments.getString(TITLE)?.let { setTitle(it) }
                setMessage(arguments.getString(MSG))
                setPositiveButton(arguments.getString(POSITIVE_TEXT) ?: getString(R.string.ok)) { _, _ ->
                    clickListener?.dialogConfirm?.invoke(arguments.getString(TAG))
                }
                setNegativeButton(arguments.getString(NEGATIVE_TEXT) ?: getString(R.string.cancel)) { _, _ ->
                    dismiss()
                }
            }.create()

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        clickListener = when {
            parentFragment is OnClickListener -> parentFragment as OnClickListener
            activity is OnClickListener -> activity as OnClickListener
            else -> null
        }
    }

    override fun onDetach() {
        clickListener = null
        super.onDetach()
    }

    companion object {
        private const val TITLE = "title"
        private const val MSG = "msg"
        private const val TAG = "tag"
        private const val POSITIVE_TEXT = "positive_text"
        private const val NEGATIVE_TEXT = "negative_text"

        fun newInstants(
                title: String? = null,
                msg: String,
                positive: String? = null,
                negative: String? = null,
                tag: String
        ) =
                ConfirmDialog().apply {
                    arguments = Bundle().apply {
                        putString(TITLE, title)
                        putString(MSG, msg)
                        putString(POSITIVE_TEXT, positive)
                        putString(NEGATIVE_TEXT, negative)
                        putString(TAG, tag)
                    }
                }
    }

    interface OnClickListener {
        val dialogConfirm: (tag: String) -> Unit
    }
}
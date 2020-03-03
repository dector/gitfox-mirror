package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_empty.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.util.visible

class EmptyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val res = context.resources

    init {
        inflate(context, R.layout.view_empty, this)
    }

    fun setRefreshListener(listener: () -> Unit) {
        refreshButton.setOnClickListener { listener() }
    }

    fun showEmptyData() {
        titleTextView.text = res.getText(R.string.empty_data)
        descriptionTextView.text = res.getText(R.string.empty_data_description)
        visible(true)
    }

    fun showEmptyError(msg: String? = null) {
        titleTextView.text = res.getText(R.string.empty_error)
        descriptionTextView.text = msg ?: res.getText(R.string.empty_error_description)
        visible(true)
    }

    fun hide() {
        visible(false)
    }
}

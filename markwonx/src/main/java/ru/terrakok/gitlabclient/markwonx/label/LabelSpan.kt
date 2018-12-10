package ru.terrakok.gitlabclient.markwonx.label

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class LabelSpan(
    val label: LabelDescription,
    val color: Int,
    // Currently unused. Provides padding for label to use in future.
    val config: LabelSpanConfig,
    val onLabelClicked: (LabelDescription) -> Unit = { }
) : ClickableSpan() {

    override fun onClick(widget: View) {
        onLabelClicked(label)
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
        ds.color = getContrastTextColorBasedOnBackgroundColor(color)
        ds.bgColor = color
    }

    /**
     * https://stackoverflow.com/questions/3942878/how-to-decide-font-color-in-white-or-black-depending-on-background-color"
     * @return Best color for text to be in contrast with background.
     */
    fun getContrastTextColorBasedOnBackgroundColor(color: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val luminosity = 0.2126 * red + 0.7152 * green + 0.0722 * blue
        return if (luminosity > 186) Color.BLACK else Color.WHITE
    }
}

data class LabelSpanConfig(
    val padding: Int
)
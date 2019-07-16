package ru.terrakok.gitlabclient.markwonx.milestone

import android.text.style.ClickableSpan
import android.view.View

class MilestoneSpan(
    val milestone: MilestoneDescription,
    val onMilestoneClick: (MilestoneDescription) -> Unit
) : ClickableSpan() {
    override fun onClick(widget: View) {
        onMilestoneClick(milestone)
    }
}
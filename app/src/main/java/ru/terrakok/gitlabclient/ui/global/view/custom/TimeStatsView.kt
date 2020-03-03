package ru.terrakok.gitlabclient.ui.global.view.custom

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.view_time_stats.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.TimeStats
import ru.terrakok.gitlabclient.util.color
import ru.terrakok.gitlabclient.util.visible

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
class TimeStatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val subHeadTextColor = context.color(R.color.subhead_text)
    private val subTitleTextColor = context.color(R.color.subtitle_text)

    init {
        inflate(context, R.layout.view_time_stats, this)
    }

    fun setTimeStats(timeStats: TimeStats?) {
        emptyTimeStats.visible(false)
        singleTimeStats.visible(false)
        progressGroup.visible(false)

        if (timeStats != null) {
            when {
                timeStats.totalTimeSpent > 0 && timeStats.timeEstimate > 0 -> initProgressViews(timeStats)
                timeStats.totalTimeSpent > 0 -> {
                    initSingleSpentView(
                        resources.getString(R.string.time_stats_spent_time),
                        timeStats.humanTotalTimeSpent,
                        singleTimeStats
                    )
                }
                timeStats.timeEstimate > 0 -> {
                    initSingleSpentView(
                        resources.getString(R.string.time_stats_estimated_time),
                        timeStats.humanTimeEstimate,
                        singleTimeStats
                    )
                }
                else -> emptyTimeStats.visible(true)
            }
        } else {
            emptyTimeStats.visible(true)
        }
    }

    private fun initProgressViews(timeStats: TimeStats) {
        setTimeSpans(
            resources.getString(R.string.time_stats_spent_time),
            timeStats.humanTotalTimeSpent,
            progressSpentTime
        )
        setTimeSpans(
            resources.getString(R.string.time_stats_est_time),
            timeStats.humanTimeEstimate,
            progressEstimatedTime
        )
        val progress = (timeStats.totalTimeSpent.toFloat() / timeStats.timeEstimate.toFloat() * 100).toInt()
        progressTimeStats.progress = progress
        progressGroup.visible(true)
    }

    private fun initSingleSpentView(title: String, value: String?, textView: TextView) {
        setTimeSpans(title, value, textView)
        textView.visible(true)
    }

    private fun setTimeSpans(title: String, value: String?, textView: TextView) {
        textView.setText(
            SpannableStringBuilder().apply {
                append(title)
                append(value)
                setSpan(
                    ForegroundColorSpan(subTitleTextColor),
                    0,
                    title.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(subHeadTextColor),
                    title.length,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            },
            TextView.BufferType.SPANNABLE
        )
    }
}

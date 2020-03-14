package ru.terrakok.gitlabclient.markwonx.label

import android.app.Activity
import android.content.Context
import android.text.Spanned
import io.noties.markwon.Markwon
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils.ID
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils.INEXISTENT
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils.MULTIPLE
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils.SINGLE
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils.SINGLE_CYRILLIC
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils.makeLabel
import ru.terrakok.gitlabclient.markwonx.simple.SimplePlugin

@RunWith(RobolectricTestRunner::class)
class SimpleLabelVisitorTest {

    private lateinit var markwon: Markwon
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = Robolectric.buildActivity(Activity::class.java).run {
            this.create()
            get()
        }
        markwon =
            Markwon
                .builder(context)
                .usePlugin(
                    SimplePlugin(
                        mapOf(
                            GitlabMarkdownExtension.LABEL to SimpleLabelVisitor(
                                LabelsTestUtils.EXISTENT_LABELS.map { it.label },
                                LabelSpanConfig(0),
                                { _, _ -> })
                        )
                    )
                )
                .build()
    }

    fun assertSpanned(label: TestLabel) {
        val content = makeLabel(label)
        val spanned = markwon.toMarkdown(content)
        assertSpanned(0, content.lastIndex, spanned, label.label)
    }

    fun assertSpanned(start: Int, end: Int, spanned: Spanned, label: LabelDescription) {
        val spans = spanned.getSpans(start, end, LabelSpan::class.java)
        assert(spans.any { span -> span.label == label })
    }

    @Test
    fun `should not create span for inexistent label`() {
        val content = makeLabel(INEXISTENT)
        val spanned = markwon.toMarkdown(content)
        val spans = spanned.getSpans(0, content.lastIndex, LabelSpan::class.java)
        assert(spans.none { span -> span.label == INEXISTENT.label })
    }

    @Test
    fun `should create span for single word label`() {
        assertSpanned(SINGLE)
    }

    @Test
    fun `should create span for cyrillic single word label`() {
        assertSpanned(SINGLE_CYRILLIC)
    }

    @Test
    fun `should create span for multiple word label`() {
        assertSpanned(MULTIPLE)
    }

    @Test
    fun `should create span for id label`() {
        assertSpanned(ID)
    }

    @Test
    fun `should create span for all types of labels in single string`() {
        val all = LabelsTestUtils.EXISTENT_LABELS
        val allStr = all.joinToString(" ") { label -> makeLabel(label) }
        val spannable = markwon.toMarkdown(allStr)
        all.forEach { label ->
            val content = label.label.name
            val start = spannable.indexOf(content)
            val end = start + content.lastIndex
            println("Asserting $spannable for $content at $start,$end: ${label.label}")
            assertSpanned(start, end, spannable, label.label)
        }
    }

}


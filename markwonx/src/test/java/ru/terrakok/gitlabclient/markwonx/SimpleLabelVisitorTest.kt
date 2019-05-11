package ru.terrakok.gitlabclient.markwonx

import android.app.Activity
import android.content.Context
import android.text.Spanned
import android.text.style.StyleSpan
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.ID
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.INEXISTENT
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.MULTIPLE
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.SINGLE
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.SINGLE_CYRILLIC
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.makeLabel
import ru.terrakok.gitlabclient.markwonx.label.*

@RunWith(RobolectricTestRunner::class)
class SimpleLabelVisitorTest {

    private lateinit var decorator: SimpleMarkdownDecorator
    private lateinit var parser: Parser
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = Robolectric.buildActivity(Activity::class.java).run {
            this.create()
            get()
        }
        decorator = SimpleMarkdownDecorator()
        val processor = SimpleExtensionProcessor()
        parser = with(Parser.Builder()) {
            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.LABEL to processor
                    )
                )
            )
            build()
        }
    }

    fun createVisitor(spannableBuilder: SpannableBuilder): Visitor {
        return SimpleVisitor(
            SpannableConfiguration.create(context),
            spannableBuilder,
            mapOf(
                GitlabMarkdownExtension.LABEL to SimpleLabelVisitor(LabelsTestUtils.EXISTENT_LABELS.map { it.label })
            )
        )
    }

    fun convertToSpannable(source: String): Spanned {
        val decorated = decorator.decorate(source)
        val node = parser.parse(decorated)
        val builder = SpannableBuilder()
        val visitor = createVisitor(builder)
        node.accept(visitor)
        return builder.text() as Spanned
    }

    fun assertSpanned(label: TestLabel) {
        val content = makeLabel(label)
        val spanned = convertToSpannable(content)
        assertSpanned(0, content.lastIndex, spanned, label.label)
    }

    fun assertSpanned(start: Int, end: Int, spanned: Spanned, label: LabelDescription) {
        val spans = spanned.getSpans(start, end, StyleSpan::class.java)
        assert(spans.any())
    }

    @Test
    fun `should not create span for inexistent label`() {
        val content = makeLabel(INEXISTENT)
        val spanned = convertToSpannable(content)
        val spans = spanned.getSpans(0, content.lastIndex, StyleSpan::class.java)
        assert(spans.none())
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
    fun `should decorate all types of labels in single string also containing normal text`() {
        val all = LabelsTestUtils.EXISTENT_LABELS
        val allStr = all.joinToString(" ") { label -> makeLabel(label) }
        val spannable = convertToSpannable(allStr)
        all.forEach { label ->
            val content = label.label.name
            val start = spannable.indexOf(content)
            val end = start + content.lastIndex
            println("Asserting $spannable for $content at $start,$end: ${label.label}")
            assertSpanned(start, end, spannable, label.label)
        }
    }

}


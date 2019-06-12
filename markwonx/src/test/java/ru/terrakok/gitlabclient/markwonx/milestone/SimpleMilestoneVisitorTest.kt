package ru.terrakok.gitlabclient.markwonx.milestone

import android.app.Activity
import android.content.Context
import android.text.Spanned
import org.commonmark.node.Visitor
import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import ru.noties.markwon.SpannableBuilder
import ru.noties.markwon.SpannableConfiguration
import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension
import ru.terrakok.gitlabclient.markwonx.label.SimpleExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.label.SimpleMarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.label.SimpleVisitor
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.ID
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.INEXISTENT
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.MULTIPLE
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.SINGLE
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.SINGLE_CYRILLIC
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.makeMilestone

@RunWith(RobolectricTestRunner::class)
class SimpleMilestoneVisitorTest {

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
        parser = with(Parser.Builder()) {
            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.MILESTONE to SimpleExtensionProcessor()
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
                GitlabMarkdownExtension.MILESTONE to SimpleMilestoneVisitor(MilestoneTestUtils.EXISTENT_MILESTONES.map { it.milestone })
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

    fun assertSpanned(milestone: TestMilestone) {
        val content = makeMilestone(milestone)
        val spanned = convertToSpannable(content)
        assertSpanned(0, content.lastIndex, spanned, milestone.milestone)
    }

    fun assertSpanned(start: Int, end: Int, spanned: Spanned, milestone: MilestoneDescription) {
        val spans = spanned.getSpans(start, end, MilestoneSpan::class.java)
        assert(spans.any { span -> span.milestone == milestone })
    }

    @Test
    fun `should not create span for inexistent milestone`() {
        val content = makeMilestone(INEXISTENT)
        val spanned = convertToSpannable(content)
        val spans = spanned.getSpans(0, content.lastIndex, MilestoneSpan::class.java)
        assert(spans.none { span -> span.milestone == INEXISTENT.milestone })
    }

    @Test
    fun `should create span for single word milestone`() {
        assertSpanned(SINGLE)
    }

    @Test
    fun `should create span for cyrillic single word milestone`() {
        assertSpanned(SINGLE_CYRILLIC)
    }

    @Test
    fun `should create span for multiple word milestone`() {
        assertSpanned(MULTIPLE)
    }

    @Test
    fun `should create span for id milestone`() {
        assertSpanned(ID)
    }

    @Test
    fun `should create span for all types of milestones in single string`() {
        val all = MilestoneTestUtils.EXISTENT_MILESTONES
        val allStr = all.joinToString(" ") { milestone -> makeMilestone(milestone) }
        val spannable = convertToSpannable(allStr)
        all.forEach { milestone ->
            val content = milestone.milestone.name
            val start = spannable.indexOf(content)
            val end = start + content.lastIndex
            println("Asserting $spannable for $content at $start,$end: ${milestone.milestone}")
            assertSpanned(start, end, spannable, milestone.milestone)
        }
    }

}


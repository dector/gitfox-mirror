package ru.terrakok.gitlabclient.markwonx

import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.label.LabelDecorator
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.label.LabelNode
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDecorator
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDescription
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneNode

class GitlabExtensionsDelimiterProcessorTest {

    private lateinit var decorator: MarkdownDecorator
    private lateinit var parser: Parser
    private lateinit var labels: List<LabelDescription>

    @Before
    fun setUp() {
        labels = listOf(LABEL)
        decorator = CompositeMarkdownDecorator(LabelDecorator(listOf(LABEL)), MilestoneDecorator())
        parser = with(Parser.Builder()) {
            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.LABEL to LabelExtensionProcessor(labels),
                        GitlabMarkdownExtension.MILESTONE to MilestoneExtensionProcessor()
                    )
                )
            )
            build()
        }
    }

    @Test
    fun `should replace decorated label extension with label node`() {
        val parsed = parser.parse(decorator.decorate("~$LABEL_STR"))
        assert(parsed.firstChild.firstChild == LabelNode(LABEL))
    }

    @Test
    fun `should replace decorated milestone extension with milestone node`() {
        val parsed = parser.parse(decorator.decorate("%$MILESTONE_STR"))
        assert(parsed.firstChild.firstChild == MilestoneNode(MilestoneDescription(name = MILESTONE_STR)))
    }

    companion object {
        const val MILESTONE_STR = "single"
        const val LABEL_STR = "single"
        val LABEL = LabelDescription(
            id = 0,
            name = LABEL_STR,
            color = "#fff"
        )
    }
}

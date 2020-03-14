package ru.terrakok.gitlabclient.markwonx

import org.commonmark.parser.Parser
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.label.LabelsTestUtils
import ru.terrakok.gitlabclient.markwonx.simple.SimpleExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.simple.SimpleMarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.simple.SimpleNode
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils

class GitlabExtensionsDelimiterProcessorTest {

    private lateinit var decorator: SimpleMarkdownDecorator
    private lateinit var parser: Parser

    @Before
    fun setUp() {
        decorator =
            SimpleMarkdownDecorator()
        val processor =
            SimpleExtensionProcessor()
        parser = with(Parser.Builder()) {
            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.LABEL to processor,
                        GitlabMarkdownExtension.MILESTONE to processor
                    )
                )
            )
            build()
        }
    }

    @Test
    fun `should replace decorated label extension with label node`() {
        val parsed = parser.parse(decorator.decorate(LabelsTestUtils.makeLabel(LabelsTestUtils.SINGLE)))
        val expected = SimpleNode(
            GitlabMarkdownExtension.LABEL,
            LabelsTestUtils.createArgsForTest(LabelsTestUtils.SINGLE)
        )
        Assert.assertEquals(parsed.firstChild.firstChild, expected)
    }

    @Test
    fun `should replace decorated milestone extension with milestone node`() {
        val parsed = parser.parse(decorator.decorate(MilestoneTestUtils.makeMilestone(MilestoneTestUtils.SINGLE)))
        val expected = SimpleNode(
            GitlabMarkdownExtension.MILESTONE,
            MilestoneTestUtils.createArgsForTest(MilestoneTestUtils.SINGLE)
        )
        Assert.assertEquals(expected, parsed.firstChild.firstChild)
    }

}

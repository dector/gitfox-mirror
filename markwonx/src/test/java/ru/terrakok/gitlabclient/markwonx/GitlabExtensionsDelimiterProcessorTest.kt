package ru.terrakok.gitlabclient.markwonx

import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.SINGLE
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.createArgsForTest
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.makeLabel
import ru.terrakok.gitlabclient.markwonx.label.SimpleExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.label.SimpleMarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.label.SimpleNode

class GitlabExtensionsDelimiterProcessorTest {

    private lateinit var decorator: SimpleMarkdownDecorator
    private lateinit var parser: Parser

    @Before
    fun setUp() {
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

    @Test
    fun `should replace decorated label extension with label node`() {
        val parsed = parser.parse(decorator.decorate(makeLabel(SINGLE)))
        assert(parsed.firstChild.firstChild == SimpleNode(GitlabMarkdownExtension.LABEL, createArgsForTest(SINGLE)))
    }

}
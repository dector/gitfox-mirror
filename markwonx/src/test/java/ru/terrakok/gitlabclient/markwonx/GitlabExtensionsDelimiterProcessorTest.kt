package ru.terrakok.gitlabclient.markwonx

import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.label.*

class GitlabExtensionsDelimiterProcessorTest {

    private lateinit var decorator: SimpleMarkdownDecorator
    private lateinit var parser: Parser
    private lateinit var labels: List<LabelDescription>

    @Before
    fun setUp() {
        labels = listOf(LABEL)
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
        val parsed = parser.parse(decorator.decorate("~$LABEL_STR"))
        assert(parsed.firstChild.firstChild == SimpleNode(GitlabMarkdownExtension.LABEL, "${LabelType.SINGLE}${GitlabMarkdownExtension.EXTENSION_OPTIONS_DELIMITER}$LABEL_STR"))
    }

    companion object {
        const val LABEL_STR = "single"
        val LABEL = LabelDescription(
            id = 0,
            name = LABEL_STR,
            color = "#fff"
        )
    }
}

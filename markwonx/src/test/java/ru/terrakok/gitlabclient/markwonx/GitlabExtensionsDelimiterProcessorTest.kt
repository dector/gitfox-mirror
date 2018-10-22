package ru.terrakok.gitlabclient.markwonx

import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.label.LabelDecorator
import ru.terrakok.gitlabclient.markwonx.label.LabelDescription
import ru.terrakok.gitlabclient.markwonx.label.LabelExtensionProcessor
import ru.terrakok.gitlabclient.markwonx.label.LabelNode

class GitlabExtensionsDelimiterProcessorTest {

    private lateinit var decorator: LabelDecorator
    private lateinit var parser: Parser
    private lateinit var labels: List<LabelDescription>

    @Before
    fun setUp() {
        labels = listOf(LABEL)
        decorator = LabelDecorator(listOf(LABEL_STR))
        parser = with(Parser.Builder()) {
            customDelimiterProcessor(
                GitlabExtensionsDelimiterProcessor(
                    mapOf(
                        GitlabMarkdownExtension.LABEL to LabelExtensionProcessor(labels)
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

    companion object {
        const val LABEL_STR = "single"
        val LABEL = LabelDescription(
            id = 0,
            name = LABEL_STR,
            color = "#fff"
        )
    }
}

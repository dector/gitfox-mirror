package ru.terrakok.gitlabclient.markwonx

import org.commonmark.node.Text
import org.commonmark.parser.delimiter.DelimiterProcessor
import org.commonmark.parser.delimiter.DelimiterRun

class GitlabExtensionsDelimiterProcessor(
    val processors: Map<GitlabMarkdownExtension, ExtensionProcessor>
) : DelimiterProcessor {
    override fun getOpeningCharacter(): Char =
        DELIMITER

    override fun getClosingCharacter(): Char =
        DELIMITER

    override fun getMinLength(): Int =
        DELIMITER_LENGTH

    override fun getDelimiterUse(opener: DelimiterRun, closer: DelimiterRun): Int =
        DELIMITER_LENGTH

    override fun process(opener: Text, closer: Text, delimiterUse: Int) {
        var text = (opener.next as Text).literal
        val extTypeStr = text.substringBefore('_')
        val extType = GitlabMarkdownExtension.valueOf(extTypeStr)
        val args = text.substringAfter('_')
        val processor = processors[extType]
        if (processor != null) {
            val replacementNode = processor.process(args)
            if (replacementNode != null) {
                opener.next.unlink()
                opener.insertAfter(replacementNode)
            }
        }
    }

    companion object {

        // Our delimiters looks like this : %%%%%foo%%%%% where %%%%% is delimiter.
        // It is so long to avoid overlapping with anything else than our custom extensions.
        const val DELIMITER_LENGTH = 5
        const val DELIMITER = '%'
        const val DELIMITER_NEGATIVE_MATCH = "(?<!(%%%%))"
        val DELIMITER_STRING = (0 until DELIMITER_LENGTH).joinToString("") { DELIMITER.toString() }

        fun decorate(extensionData: String) = StringBuilder().apply {
            append(GitlabExtensionsDelimiterProcessor.DELIMITER_STRING)
            append(extensionData)
            append(GitlabExtensionsDelimiterProcessor.DELIMITER_STRING)
        }.toString()

    }

}
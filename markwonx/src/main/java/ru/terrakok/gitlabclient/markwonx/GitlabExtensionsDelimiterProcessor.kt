package ru.terrakok.gitlabclient.markwonx

import org.commonmark.node.Text
import org.commonmark.parser.delimiter.DelimiterProcessor
import org.commonmark.parser.delimiter.DelimiterRun

class GitlabExtensionsDelimiterProcessor(
    private val processors: Map<GitlabMarkdownExtension, ExtensionProcessor>
) : DelimiterProcessor {

    override fun getOpeningCharacter(): Char = DELIMITER_START
    override fun getClosingCharacter(): Char = DELIMITER_START
    override fun getMinLength(): Int = DELIMITER_LENGTH
    override fun getDelimiterUse(opener: DelimiterRun, closer: DelimiterRun): Int = DELIMITER_LENGTH

    override fun process(opener: Text, closer: Text, delimiterUse: Int) {
        val text = (opener.next as Text).literal
        val extTypeStr = text.substringBefore(GitlabMarkdownExtension.EXTENSION_OPTIONS_DELIMITER)
        val extType = GitlabMarkdownExtension.valueOf(extTypeStr)
        val args = text.substringAfter(GitlabMarkdownExtension.EXTENSION_OPTIONS_DELIMITER)
        val processor = processors[extType]
        if (processor != null) {
            val replacementNode = processor.process(extType, args)
            if (replacementNode != null) {
                opener.next.unlink()
                opener.insertAfter(replacementNode)
            }
        }
    }

    companion object {

        // Our delimiters looks like this : ▘foo▗ where ▘ is starting delimiter and ▗ is ending delimiter.
        const val DELIMITER_LENGTH = 1
        const val DELIMITER_START = '\u2598'
        const val DELIMITER_START_STR = DELIMITER_START.toString()
        const val DELIMITER_END = '\u2597'
        const val DELIMITER_END_STR = DELIMITER_END.toString()
    }

}
package ru.terrakok.gitlabclient.markwonx

interface MarkdownClickListener {

    /**
     * @return true if event was consumed
     */
    fun onMarkdownClick(extension: GitlabMarkdownExtension, value: Any): Boolean
}
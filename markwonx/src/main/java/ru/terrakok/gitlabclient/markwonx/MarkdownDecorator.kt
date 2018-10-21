package ru.terrakok.gitlabclient.markwonx

interface MarkdownDecorator {
    fun decorate(markdown: String): String
}
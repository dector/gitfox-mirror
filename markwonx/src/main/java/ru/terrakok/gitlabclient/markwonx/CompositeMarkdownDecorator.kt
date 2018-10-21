package ru.terrakok.gitlabclient.markwonx

class CompositeMarkdownDecorator constructor(
    private vararg val decorators: MarkdownDecorator
) : MarkdownDecorator {
    override fun decorate(markdown: String): String = decorators.fold(markdown) { acc, decorator ->
        decorator.decorate(acc)
    }
}
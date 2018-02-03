package ru.terrakok.gitlabclient.model.repository.tools

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter {

    companion object {
        private val parser = Parser.builder().build()
        private val renderer = HtmlRenderer.builder().build()
    }

    fun markdownToHtml(raw: String) = renderer.render(parser.parse(raw))
}
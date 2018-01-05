package ru.terrakok.gitlabclient.model.repository.tools

import com.commonsware.cwac.anddown.AndDown

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class MarkDownConverter {
    private val converter = AndDown()

    fun markdownToHtml(raw: String) = converter.markdownToHtml(raw)
}
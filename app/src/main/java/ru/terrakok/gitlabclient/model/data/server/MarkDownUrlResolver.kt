package ru.terrakok.gitlabclient.model.data.server

import ru.terrakok.gitlabclient.entity.Project
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy) on 09.04.18.
 */
class MarkDownUrlResolver @Inject constructor() {

    private val regex = Regex("(!\\[.+]\\(/uploads/.+/.+\\.\\w{3,4}\\))")

    fun resolve(body: String, project: Project): String {
        val iterator = regex.findAll(body).iterator()
        val builderBody = StringBuilder(body)
        if (iterator.hasNext()) {
            do {
                val result = iterator.next()
                builderBody.insert(builderBody.indexOf("/uploads/", result.range.start), project.pathWithNamespace)
            } while (iterator.hasNext())
        }
        return builderBody.toString()
    }
}
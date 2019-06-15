package ru.terrakok.gitlabclient.model.data.server

import javax.inject.Inject
import ru.terrakok.gitlabclient.entity.Project

/**
 * @author Eugene Shapovalov (CraggyHaggy) on 09.04.18.
 */
class MarkDownUrlResolver @Inject constructor() {

    private val regex = Regex("!\\[[^]]+]\\(/uploads/[^)]+\\)")

    fun resolve(body: String, project: Project): String {
        val builderBody = StringBuilder(body)
        regex.findAll(body).forEach {
            builderBody.insert(builderBody.indexOf("/uploads/", it.range.start), project.pathWithNamespace)
        }
        return builderBody.toString()
    }
}
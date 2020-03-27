package ru.terrakok.gitlabclient.util

import ru.terrakok.gitlabclient.entity.Project

private val regex = Regex("!\\[[^]]+]\\(/uploads/[^)]+\\)")
fun String.resolveMarkdownUrl(project: Project): String {
    val builderBody = StringBuilder(this)
    regex.findAll(this).forEach {
        builderBody.insert(builderBody.indexOf("/uploads/", it.range.start), project.pathWithNamespace)
    }
    return builderBody.toString()
}

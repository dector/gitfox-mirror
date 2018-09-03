package ru.terrakok.gitlabclient.presentation.global

import ru.terrakok.gitlabclient.entity.Project
import javax.inject.Inject

class MarkDownUrlResolver @Inject constructor() {

    private val regex = Regex("^!\\[\\w+]\\(/uploads/\\w+/\\w+\\.\\w+\\)$")

    // ![CragHag](/uploads/69c4ef83b86c66eb3f147915d26c427e/CragHag.png) - before attach
    // https://gitlab.com/terrakok/gitlab-client/uploads/b4048510da2ba117cdc793007066bc25/CragHag.png link to download
    // https://gitlab.com/CraggyHaggy/GandastBot/uploads/9475c799f5e9ad5cd1a8ce28ce652ff9/citadel.jpg

    fun resolve(body: String, project: Project): String {
        return if (regex.matches(body)) {
            StringBuilder(body)
                .insert(body.indexOf("/uploads/"), project.pathWithNamespace)
                .toString()
        } else {
            body
        }
    }
}
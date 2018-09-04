package ru.terrakok.gitlabclient.presentation.global

import ru.terrakok.gitlabclient.entity.Project
import javax.inject.Inject

class MarkDownUrlResolver @Inject constructor() {

    private val regex = Regex("^!\\[.+]\\(/uploads/.+/.+\\.\\w{3,4}\\)$")

    // ![CragHag](/uploads/69c4ef83b86c66eb3f147915d26c427e/CragHag.png) - before attach
    // ![2018-09-03_15.26.43](/uploads/c1fc914375a3d975f12bb6d54d1ee8c8/2018-09-03_15.26.43.jpg)
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
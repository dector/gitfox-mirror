package ru.terrakok.gitlabclient.entity.app.file

class ProjectFileNavigation(
    var defaultPath: String,
    var branchName: String,
    val paths: ArrayList<String>
) {

    constructor() : this("", "", arrayListOf())

    fun isInitiated() = defaultPath.isNotEmpty() && branchName.isNotEmpty() && paths.isNotEmpty()

    fun isInRoot() = paths.size == 1

    fun addPath(path: String) = paths.add(path)

    fun removeTopPath() = paths.removeAt(paths.lastIndex)

    fun clearPaths() {
        if (!isInRoot()) {
            paths.clear()
            paths.add(ROOT_PATH)
        }
    }

    fun getUIPath() =
        if (isInRoot()) {
            defaultPath
        } else {
            "$defaultPath$UI_SEPARATOR${paths.subList(1, paths.size).joinToString(separator = UI_SEPARATOR)}"
        }

    fun getRemotePath() =
        if (isInRoot()) {
            ROOT_PATH
        } else {
            paths.subList(1, paths.size).joinToString(separator = REMOTE_SEPARATOR)
        }

    companion object {
        const val ROOT_PATH = ""
        private const val UI_SEPARATOR = " / "
        private const val REMOTE_SEPARATOR = "/"
    }
}
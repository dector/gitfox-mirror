package ru.terrakok.gitlabclient.markwonx.label;

enum class LabelType(
    val regex: Regex
) {
    SINGLE("~([a-zA-Z]+)".toRegex()),
    MULTIPLE("~\"(.*?)\"".toRegex()),
    ID("~(\\d+)".toRegex());

    companion object {
        fun byString(value: String): LabelType? = LabelType.values().firstOrNull { it.toString() == value }
    }
}
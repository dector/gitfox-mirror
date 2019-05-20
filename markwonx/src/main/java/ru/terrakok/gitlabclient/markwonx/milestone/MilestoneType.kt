package ru.terrakok.gitlabclient.markwonx.milestone;

import ru.terrakok.gitlabclient.markwonx.label.LabelType

enum class MilestoneType(
    val weight: Int,
    val regex: Regex
) {
    ID(0, "%(\\d+)".toRegex()),
    SINGLE(1, "%([a-zA-Z]+)".toRegex()),
    MULTIPLE(2, "%\"(.*?)\"".toRegex());

    companion object {
        fun byString(value: String): MilestoneType? = values().firstOrNull { it.toString() == value }

        fun sortedValues() = LabelType.values().sortedBy { it.weight }
    }
}
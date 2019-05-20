package ru.terrakok.gitlabclient.markwonx.milestone;

enum class MilestoneType(
    val weight: Int,
    val regex: Regex
) {
    ID(0, "%(\\d+)".toRegex()),
    SINGLE(1, "%([\\p{L}0-9]+)".toRegex()),
    MULTIPLE(2, "%\"([\\p{L}0-9\\s]+?)\"".toRegex());

    companion object {
        fun byString(value: String): MilestoneType? = values().firstOrNull { it.toString() == value }

        fun sortedValues() = values().sortedBy { it.weight }
    }
}
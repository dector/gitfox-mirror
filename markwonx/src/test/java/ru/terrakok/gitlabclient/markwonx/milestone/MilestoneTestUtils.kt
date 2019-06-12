package ru.terrakok.gitlabclient.markwonx.milestone

import ru.terrakok.gitlabclient.markwonx.GitlabExtensionsDelimiterProcessor
import ru.terrakok.gitlabclient.markwonx.GitlabMarkdownExtension

object MilestoneTestUtils {
    val INEXISTENT = TestMilestone(
        MilestoneType.SINGLE,
        MilestoneDescription(0, "inexistent")
    )
    val SINGLE = TestMilestone(
        MilestoneType.SINGLE,
        MilestoneDescription(0, "single")
    )
    val SINGLE_CYRILLIC = TestMilestone(
        MilestoneType.SINGLE,
        MilestoneDescription(1, "РусскийОдноСлово")
    )
    val MULTIPLE = TestMilestone(
        MilestoneType.MULTIPLE,
        MilestoneDescription(2, "Multiword english")
    )
    val ID = TestMilestone(
        MilestoneType.ID,
        MilestoneDescription(3, "Test ID Milestone")
    )

    val EXISTENT_MILESTONES by lazy { listOf(
        SINGLE,
        SINGLE_CYRILLIC,
        MULTIPLE,
        ID
    ) }

    fun getMilestoneContent(milestone: TestMilestone): String {
        return when (milestone.type) {
            MilestoneType.ID -> milestone.milestone.id.toString()
            else -> milestone.milestone.name
        }
    }

    fun makeMilestone(label: TestMilestone): String {
        val content = getMilestoneContent(label)
        return when (label.type) {
            MilestoneType.MULTIPLE -> "%\"$content\""
            else -> "%$content"
        }
    }

    fun createArgsForTest(milestone: TestMilestone): String {
        return "${milestone.type}${GitlabMarkdownExtension.OPTS_DELIMITER}${getMilestoneContent(
            milestone
        )}"
    }

    fun decorateForTest(milestone: TestMilestone): String {
        val args = createArgsForTest(milestone)
        return listOf(
            GitlabExtensionsDelimiterProcessor.DELIMITER_START,
            GitlabMarkdownExtension.MILESTONE,
            GitlabMarkdownExtension.OPTS_DELIMITER,
            args,
            GitlabExtensionsDelimiterProcessor.DELIMITER_END
        ).joinToString()
    }

}


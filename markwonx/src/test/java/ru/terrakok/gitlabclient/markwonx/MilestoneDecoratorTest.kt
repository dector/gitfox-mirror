package ru.terrakok.gitlabclient.markwonx

import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneDecorator
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneType

class MilestoneDecoratorTest {

    lateinit var decorator: MilestoneDecorator

    @Before
    fun setUp() {
        decorator = MilestoneDecorator()
    }

    fun assertDecorated(milestoneType: MilestoneType, source: String, name: String) {
        assert(decorator.decorate(source) == MilestoneDecorator.getDecoratedString(milestoneType, name))
    }

    @Test
    fun `should decorate single word milestone`() {
        assertDecorated(MilestoneType.SINGLE, SINGLE, SINGLE_NAME)
    }

    @Test
    fun `should decorate cyrillic single word milestone`() {
        assertDecorated(MilestoneType.MULTIPLE, SINGLE_CYRILLIC, SINGLE_CYRILLIC_NAME)
    }

    @Test
    fun `should decorate multiple word milestone`() {
        assertDecorated(MilestoneType.MULTIPLE, MULTIPLE, MULTIPLE_NAME)
    }

    @Test
    fun `should decorate id milestone`() {
        assertDecorated(MilestoneType.ID, ID, ID_ONLY.toString())
    }

    @Test
    fun `should decorate all types of milestones in single string also containing normal text`() {
        val all = listOf(
            TestMilestone(MilestoneType.SINGLE, SINGLE, SINGLE_NAME),
            TestMilestone(MilestoneType.MULTIPLE, SINGLE_CYRILLIC, SINGLE_CYRILLIC_NAME),
            TestMilestone(MilestoneType.MULTIPLE, MULTIPLE, MULTIPLE_NAME),
            TestMilestone(MilestoneType.ID, ID, ID_ONLY.toString())
        )
        val allStr = all.map { (_, source, _) -> source }
        val allResults = all.map { (type, _, name) -> MilestoneDecorator.getDecoratedString(type, name) }
        val fullStr = "some another text in the beginning " +
            "${allStr.joinToString(" ")} " +
            "some another text in the end"
        val expectedResult = "some another text in the beginning " +
            "${allResults.joinToString(" ")} " +
            "some another text in the end"
        assert(decorator.decorate(fullStr) == expectedResult)
    }

    companion object {
        const val SINGLE = "%single"
        const val SINGLE_NAME = "single"
        const val SINGLE_CYRILLIC = "%\"РусскийОдноСлово\""
        const val SINGLE_CYRILLIC_NAME = "РусскийОдноСлово"
        const val MULTIPLE = "%\"Multiword english\""
        const val MULTIPLE_NAME = "Multiword english"
        const val ID = "%0"
        const val ID_ONLY = 0
    }

    data class TestMilestone(val type: MilestoneType, val source: String, val name: String)
}
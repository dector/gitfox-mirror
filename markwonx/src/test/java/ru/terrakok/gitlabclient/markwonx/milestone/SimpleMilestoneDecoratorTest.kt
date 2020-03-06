package ru.terrakok.gitlabclient.markwonx.milestone

import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.simple.SimpleMarkdownDecorator
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.ID
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.MULTIPLE
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.SINGLE
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.SINGLE_CYRILLIC
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.decorateForTest
import ru.terrakok.gitlabclient.markwonx.milestone.MilestoneTestUtils.makeMilestone

class SimpleMilestoneDecoratorTest {

    private lateinit var decorator: SimpleMarkdownDecorator

    @Before
    fun setUp() {
        decorator =
            SimpleMarkdownDecorator()
    }

    fun assertDecorated(milestone: TestMilestone) {
        val decorated = decorator.decorate(makeMilestone(milestone))
        val testDecorated = decorateForTest(milestone)
        println("asserting decoration: $decorated to $testDecorated")
        assert(decorated == testDecorated)
    }

    @Test
    fun `should decorate single word milestone`() {
        assertDecorated(SINGLE)
    }

    @Test
    fun `should decorate cyrillic single word milestone`() {
        assertDecorated(SINGLE_CYRILLIC)
    }

    @Test
    fun `should decorate multiple word milestone`() {
        assertDecorated(MULTIPLE)
    }

    @Test
    fun `should decorate id milestone`() {
        assertDecorated(ID)
    }

    @Test
    fun `should decorate all types of milestones in single string also containing normal text`() {
        val all = MilestoneTestUtils.EXISTENT_MILESTONES
        val allStr = all.map { milestone -> makeMilestone(milestone) }
        val allResults = all.map { milestone -> decorateForTest(milestone) }
        val fullStr = "some another text in the beginning " +
            "${allStr.joinToString(" ")} " +
            "some another text in the end"
        val expectedResult = "some another text in the beginning " +
            "${allResults.joinToString(" ")} " +
            "some another text in the end"
        assert(decorator.decorate(fullStr) == expectedResult)
    }

}
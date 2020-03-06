package ru.terrakok.gitlabclient.markwonx

import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.ID
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.MULTIPLE
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.SINGLE
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.SINGLE_CYRILLIC
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.decorateForTest
import ru.terrakok.gitlabclient.markwonx.LabelsTestUtils.makeLabel
import ru.terrakok.gitlabclient.markwonx.simple.SimpleMarkdownDecorator

class SimpleLabelDecoratorTest {

    private lateinit var decorator: SimpleMarkdownDecorator

    @Before
    fun setUp() {
        decorator =
            SimpleMarkdownDecorator()
    }

    fun assertDecorated(label: TestLabel) {
        val decorated = decorator.decorate(makeLabel(label))
        val testDecorated = decorateForTest(label)
        println("asserting decoration: $decorated to $testDecorated")
        assert(decorated == testDecorated)
    }

    @Test
    fun `should decorate single word label`() {
        assertDecorated(SINGLE)
    }

    @Test
    fun `should decorate cyrillic single word label`() {
        assertDecorated(SINGLE_CYRILLIC)
    }

    @Test
    fun `should decorate multiple word label`() {
        assertDecorated(MULTIPLE)
    }

    @Test
    fun `should decorate id label`() {
        assertDecorated(ID)
    }

    @Test
    fun `should decorate all types of labels in single string also containing normal text`() {
        val all = LabelsTestUtils.EXISTENT_LABELS
        val allStr = all.map { label -> makeLabel(label) }
        val allResults = all.map { label -> decorateForTest(label) }
        val fullStr = "some another text in the beginning " +
            "${allStr.joinToString(" ")} " +
            "some another text in the end"
        val expectedResult = "some another text in the beginning " +
            "${allResults.joinToString(" ")} " +
            "some another text in the end"
        assert(decorator.decorate(fullStr) == expectedResult)
    }

}
package ru.terrakok.gitlabclient.markwonx

import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.markwonx.label.LabelDecorator
import ru.terrakok.gitlabclient.markwonx.label.LabelType

class LabelDecoratorTest {

    lateinit var decorator: LabelDecorator

    @Before
    fun setUp() {
        decorator = LabelDecorator()
    }

    fun assertDecorated(labelType: LabelType, source: String, name: String) {
        assert(decorator.decorate(source) == decorateForTest(labelType, name))
    }

    private fun decorateForTest(labelType: LabelType, name: String): Any? {
        return "%%%%%LABEL_${labelType}_$name%%%%%"
    }

    @Test
    fun `should decorate single word label`() {
        assertDecorated(LabelType.SINGLE, SINGLE, SINGLE_NAME)
    }

    @Test
    fun `should decorate cyrillic single word label`() {
        assertDecorated(LabelType.MULTIPLE, SINGLE_CYRILLIC, SINGLE_CYRILLIC_NAME)
    }

    @Test
    fun `should decorate multiple word label`() {
        assertDecorated(LabelType.MULTIPLE, MULTIPLE, MULTIPLE_NAME)
    }

    @Test
    fun `should not decorate inexistent label`() {
        assert(decorator.decorate(INEXISTENT) == INEXISTENT)
    }

    @Test
    fun `should decorate id label`() {
        assertDecorated(LabelType.ID, ID, ID_ONLY)
    }

    @Test
    fun `should decorate all types of labels in single string also containing normal text`() {
        val all = listOf(
            TestLabel(LabelType.SINGLE, SINGLE, SINGLE_NAME),
            TestLabel(LabelType.MULTIPLE, SINGLE_CYRILLIC, SINGLE_CYRILLIC_NAME),
            TestLabel(LabelType.MULTIPLE, MULTIPLE, MULTIPLE_NAME),
            TestLabel(LabelType.ID, ID, ID_ONLY)
        )
        val allStr = all.map { (_, source, _) -> source }
        val allResults = all.map { (type, _, name) -> decorateForTest(type, name) }
        val fullStr = "some another text in the beginning " +
            "${allStr.joinToString(" ")} " +
            "some another text in the end"
        val expectedResult = "some another text in the beginning " +
            "${allResults.joinToString(" ")} " +
            "some another text in the end"
        assert(decorator.decorate(fullStr) == expectedResult)
    }

    companion object {
        const val INEXISTENT = "~inexistent"
        const val SINGLE = "~single"
        const val SINGLE_NAME = "single"
        const val SINGLE_CYRILLIC = "~\"РусскийОдноСлово\""
        const val SINGLE_CYRILLIC_NAME = "РусскийОдноСлово"
        const val MULTIPLE = "~\"Multiword english\""
        const val MULTIPLE_NAME = "Multiword english"
        const val ID = "~0"
        const val ID_ONLY = "0"
    }

    data class TestLabel(val type: LabelType, val source: String, val name: String)
}
package ru.terrakok.gitlabclient.model.interactor

import com.nhaarman.mockitokotlin2.anyOrNull
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito
import org.mockito.Mockito.times
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * Created by Vitaliy Belyaev on 11.06.2019.
 */
class TodoInteractorTest {
    private val defaultPageSize = 1
    private val testPage = 2
    private val testTodo = TestData.getTodo()
    private val testUser = TestData.getUser()

    private val api = Mockito.mock(GitlabApi::class.java)
    private val interactor = TodoInteractor(
        api,
        ServerChanges(TestSchedulers()),
        TestSchedulers(),
        PrimitiveWrapper(defaultPageSize)
    )

    @Test
    fun `get todos should succeed with valid api response`() {
        // GIVEN
        val expectedTargetHeader = TestData.getTargetHeaderForTodo(testTodo, testUser)

        given(api.getTodos(
                anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyInt(), anyInt())).willReturn(Single.just(listOf(testTodo)))

        // WHEN
        val testObserver = interactor.getTodos(currentUser = testUser, page = testPage).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .getTodos(null, null, null, null,
                        null, testPage, defaultPageSize)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(listOf(expectedTargetHeader))
    }

    @Test
    fun `mark pending todo done should return todo with done state`() {
        // GIVEN
        val doneTodo = testTodo.copy(state = TodoState.DONE)
        given(api.markPendingTodoAsDone(anyLong())).willReturn(Single.just(doneTodo))

        // WHEN
        val testObserver = interactor.markPendingTodoAsDone(testTodo.id).test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .markPendingTodoAsDone(testTodo.id)

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertResult(doneTodo)
    }

    @Test
    fun `mark all pending todos done should return complete with valid api response`() {
        // GIVEN
        given(api.markAllPendingTodosAsDone()).willReturn(Completable.complete())

        // WHEN
        val testObserver = interactor.markAllPendingTodosAsDone().test()
        testObserver.awaitTerminalEvent()

        // THEN
        then(api)
                .should(times(1))
                .markAllPendingTodosAsDone()

        then(api).shouldHaveNoMoreInteractions()

        testObserver.assertNoErrors()
                .assertNoValues()
                .assertComplete()
    }
}

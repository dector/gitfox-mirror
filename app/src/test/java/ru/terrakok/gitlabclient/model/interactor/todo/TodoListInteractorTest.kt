package ru.terrakok.gitlabclient.model.interactor.todo

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.repository.todo.TodoRepository

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 21.09.17
 */
class TodoListInteractorTest {

    private lateinit var interactor: TodoListInteractor
    private lateinit var todoRepository: TodoRepository

    private val page = 0
    private val error = RuntimeException()

    @Before
    fun setUp() {
        todoRepository = mock<TodoRepository>()
        interactor = TodoListInteractor(todoRepository)
    }

    @Test
    fun getMyPendingTodos_success() {
        whenever(todoRepository.getTodos(anyOrNull(), anyOrNull(), anyOrNull(),
                eq(TodoState.PENDING), anyOrNull(), eq(page), any())).thenReturn(getFakeTodos())

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
                eq(null),
                eq(null),
                eq(null),
                eq(TodoState.PENDING),
                eq(null),
                eq(page),
                any()
        )
        observer
                .assertValueCount(1)
                .assertNoErrors()
        assert(observer.values().size == 3)
    }

    @Test
    fun getMyPendingTodos_error() {
        whenever(todoRepository.getTodos(anyOrNull(), anyOrNull(), anyOrNull(),
                eq(TodoState.PENDING), anyOrNull(), eq(page), any())).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
                eq(null),
                eq(null),
                eq(null),
                eq(TodoState.PENDING),
                eq(null),
                eq(page),
                any()
        )
        observer
                .assertNoValues()
                .assertError(error)
    }

    @Test
    fun getMyDoneTodos_success() {
        whenever(todoRepository.getTodos(anyOrNull(), anyOrNull(), anyOrNull(),
                eq(TodoState.DONE), anyOrNull(), eq(page), any())).thenReturn(getFakeTodos())

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
                eq(null),
                eq(null),
                eq(null),
                eq(TodoState.DONE),
                eq(null),
                eq(page),
                any()
        )
        observer
                .assertValueCount(1)
                .assertNoErrors()
        assert(observer.values().size == 3)
    }

    @Test
    fun getMyDoneTodos_error() {
        whenever(todoRepository.getTodos(anyOrNull(), anyOrNull(), anyOrNull(),
                eq(TodoState.DONE), anyOrNull(), eq(page), any())).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
                eq(null),
                eq(null),
                eq(null),
                eq(TodoState.DONE),
                eq(null),
                eq(page),
                any()
        )
        observer
                .assertNoValues()
                .assertError(error)
    }

    private fun getFakeTodos() = Single.just(
            listOf<Todo>(mock<Todo>(), mock<Todo>(), mock<Todo>())
    )
}
package ru.terrakok.gitlabclient.model.interactor.todo

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.interactor.ProfileInteractor
import ru.terrakok.gitlabclient.model.interactor.TodoInteractor

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 21.09.17
 */
class TodoInteractorTest {

    private lateinit var interactor: TodoInteractor
    private lateinit var todoInteractor: TodoInteractor
    private lateinit var profileInteractor: ProfileInteractor

    private val page = 0
    private val error = RuntimeException()
    private val currentUser = mock<User>()

    @Before
    fun setUp() {
        todoInteractor = mock()
        profileInteractor = mock()
        interactor = TodoInteractor(
            todoInteractor,
            profileInteractor
        )
    }

    @Test
    fun getMyPendingTodos_success() {
        whenever(profileInteractor.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoInteractor.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(getTodos())

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(todoInteractor).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.PENDING),
            eq(null),
            eq(page),
            any()
        )
        verify(profileInteractor).getMyProfile()
        observer
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue { it.size == 3 }
    }

    @Test
    fun getMyPendingTodos_errorFromTodoRepository() {
        whenever(profileInteractor.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoInteractor.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(todoInteractor).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.PENDING),
            eq(null),
            eq(page),
            any()
        )
        verify(profileInteractor).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    @Test
    fun getMyPendingTodos_errorFromMyProfileInteractor() {
        whenever(profileInteractor.getMyProfile()).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(profileInteractor).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    @Test
    fun getMyDoneTodos_success() {
        whenever(profileInteractor.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoInteractor.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(getTodos())

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(todoInteractor).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.DONE),
            eq(null),
            eq(page),
            any()
        )
        verify(profileInteractor).getMyProfile()
        observer
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue { it.size == 3 }
    }

    @Test
    fun getMyDoneTodos_errorFromTodoRepository() {
        whenever(profileInteractor.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoInteractor.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(todoInteractor).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.DONE),
            eq(null),
            eq(page),
            any()
        )
        verify(profileInteractor).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    @Test
    fun getMyDoneTodos_errorFromMyProfileInteractor() {
        whenever(profileInteractor.getMyProfile()).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(profileInteractor).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    private fun getTodos() = Single.just(
        listOf(mock<TargetHeader>(), mock<TargetHeader>(), mock<TargetHeader>())
    )
}

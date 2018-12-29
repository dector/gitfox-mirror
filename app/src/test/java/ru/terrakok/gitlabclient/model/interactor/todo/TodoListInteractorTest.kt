package ru.terrakok.gitlabclient.model.interactor.todo

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.todo.TodoRepository

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 21.09.17
 */
class TodoListInteractorTest {

    private lateinit var interactor: TodoListInteractor
    private lateinit var todoRepository: TodoRepository
    private lateinit var profileRepository: ProfileRepository

    private val page = 0
    private val error = RuntimeException()
    private val currentUser = mock<User>()

    @Before
    fun setUp() {
        todoRepository = mock()
        profileRepository = mock()
        interactor = TodoListInteractor(todoRepository, profileRepository)
    }

    @Test
    fun getMyPendingTodos_success() {
        whenever(profileRepository.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoRepository.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(getTodos())

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.PENDING),
            eq(null),
            eq(page),
            any()
        )
        verify(profileRepository).getMyProfile()
        observer
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue { it.size == 3 }
    }

    @Test
    fun getMyPendingTodos_errorFromTodoRepository() {
        whenever(profileRepository.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoRepository.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.PENDING),
            eq(null),
            eq(page),
            any()
        )
        verify(profileRepository).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    @Test
    fun getMyPendingTodos_errorFromMyProfileInteractor() {
        whenever(profileRepository.getMyProfile()).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(true, page).test()
        observer.awaitTerminalEvent()

        verify(profileRepository).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    @Test
    fun getMyDoneTodos_success() {
        whenever(profileRepository.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoRepository.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(getTodos())

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.DONE),
            eq(null),
            eq(page),
            any()
        )
        verify(profileRepository).getMyProfile()
        observer
            .assertValueCount(1)
            .assertNoErrors()
            .assertValue { it.size == 3 }
    }

    @Test
    fun getMyDoneTodos_errorFromTodoRepository() {
        whenever(profileRepository.getMyProfile()).thenReturn(Single.just(currentUser))
        whenever(
            todoRepository.getTodos(
                any(), anyOrNull(), anyOrNull(), anyOrNull(),
                anyOrNull(), anyOrNull(), any(), any()
            )
        ).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(todoRepository).getTodos(
            eq(currentUser),
            eq(null),
            eq(null),
            eq(null),
            eq(TodoState.DONE),
            eq(null),
            eq(page),
            any()
        )
        verify(profileRepository).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    @Test
    fun getMyDoneTodos_errorFromMyProfileInteractor() {
        whenever(profileRepository.getMyProfile()).thenReturn(Single.error(error))

        val observer = interactor.getMyTodos(false, page).test()
        observer.awaitTerminalEvent()

        verify(profileRepository).getMyProfile()
        observer
            .assertNoValues()
            .assertError(error)
    }

    private fun getTodos() = Single.just(
        listOf(mock<TargetHeader>(), mock<TargetHeader>(), mock<TargetHeader>())
    )
}

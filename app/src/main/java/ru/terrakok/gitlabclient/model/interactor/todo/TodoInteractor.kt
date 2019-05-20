package ru.terrakok.gitlabclient.model.interactor.todo

import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.todo.TodoRepository
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 21.09.17
 */
class TodoInteractor @Inject constructor(
    private val todoRepository: TodoRepository,
    private val profileRepository: ProfileRepository
) {
    fun getMyTodos(
        isPending: Boolean,
        page: Int
    ) = profileRepository
        .getMyProfile()
        .flatMap { currentUser ->
            todoRepository.getTodos(
                currentUser = currentUser,
                state = if (isPending) TodoState.PENDING else TodoState.DONE,
                page = page
            )
        }

    fun getMyAssignedTodoCount() = todoRepository.getMyAssignedTodoCount()
}
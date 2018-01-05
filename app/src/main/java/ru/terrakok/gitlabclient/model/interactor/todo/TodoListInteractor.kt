package ru.terrakok.gitlabclient.model.interactor.todo

import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.repository.todo.TodoRepository
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 21.09.17
 */
class TodoListInteractor @Inject constructor(
        private val todoRepository: TodoRepository,
        private val myProfileInteractor: MyProfileInteractor
) {
    fun getMyTodos(
            isPending: Boolean,
            page: Int
    ) = myProfileInteractor
            .getMyProfile()
            .flatMap { myUserInfo ->
                todoRepository.getTodos(
                        myUserInfo = myUserInfo,
                        state = if (isPending) TodoState.PENDING else TodoState.DONE,
                        page = page
                )
            }
}
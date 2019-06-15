package ru.terrakok.gitlabclient.model.interactor.user

import javax.inject.Inject
import ru.terrakok.gitlabclient.model.repository.user.UserRepository

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserInteractor @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getUser(id: Long) = userRepository.getUser(id)
}
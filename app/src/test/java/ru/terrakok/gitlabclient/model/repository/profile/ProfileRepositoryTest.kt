package ru.terrakok.gitlabclient.model.repository.profile

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.data.server.GitlabApi

/**
 * @author Artur Badretdinov (Gaket)
 *         31.05.17.
 *
 * @author Vitaliy Belyaev on 18.05.19.
 *
 */
class ProfileRepositoryTest {

    private val testServer = "Test server"
    private val testError = RuntimeException("test error")
    private val testUser = TestData.getUser()

    private val api: GitlabApi = mock()
    private val profileRepo = ProfileRepository(testServer, api, TestSchedulers())

    @Test
    fun get_user() {
        `when`(api.getMyUser()).thenReturn(Single.just(testUser))

        val testObserver: TestObserver<User> = profileRepo.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(api).getMyUser()

        testObserver
                .assertNoErrors()
                .assertComplete()
                .assertValue(testUser)
    }

    @Test
    fun get_user_error() {
        `when`(api.getMyUser()).thenReturn(Single.error(testError))

        val testObserver: TestObserver<User> = profileRepo.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(api).getMyUser()

        testObserver
                .assertNoValues()
                .assertError(testError)
    }

    @Test
    fun get_my_server_name() {
        val result = profileRepo.getMyServerName()
        Assert.assertEquals(testServer, result)
    }
}
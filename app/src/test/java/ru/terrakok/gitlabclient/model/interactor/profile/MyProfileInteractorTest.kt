package ru.terrakok.gitlabclient.model.interactor.profile

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.entity.app.MyUserInfo
import ru.terrakok.gitlabclient.entity.common.User
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import java.util.*

/**
 * @author Artur Badretdinov (Gaket)
 *         31.05.17.
 */
class MyProfileInteractorTest {

    @Test
    fun get_user_info() {
        val testUser = User(1L, null, null, null, null, null,
                null, Date(), false, null, null, null, null, null,
                null, null, Date(), Date(), 0L, 0L, Date(), null, false, false, false, false)

        val profileRepo = mock(ProfileRepository::class.java)
        `when`(profileRepo.getMyProfile()).thenReturn(Single.just(testUser))
        val testServer = "Test server"
        `when`(profileRepo.getMyServerName()).thenReturn(testServer)

        val authRepo = mock(AuthRepository::class.java)
        `when`(authRepo.getSignState()).thenReturn(Observable.just(true))

        val interactor = MyProfileInteractor(authRepo, profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo, times(1)).getMyProfile()
        val testUserInfo = MyUserInfo(testUser, testServer)
        testObserver
                .assertValueCount(1)
                .assertValue(testUserInfo)
                .assertNoErrors()
    }

    @Test
    fun get_user_info_error() {
        val error = RuntimeException("test error")

        val profileRepo = mock(ProfileRepository::class.java)
        `when`(profileRepo.getMyProfile()).thenReturn(Single.error(error))
        `when`(profileRepo.getMyServerName()).thenReturn("Test server")

        val authRepo = mock(AuthRepository::class.java)
        `when`(authRepo.getSignState()).thenReturn(Observable.just(true))

        val interactor = MyProfileInteractor(authRepo, profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo, times(1)).getMyProfile()
        testObserver
                .assertValueCount(0)
                .assertError(error)
    }
}
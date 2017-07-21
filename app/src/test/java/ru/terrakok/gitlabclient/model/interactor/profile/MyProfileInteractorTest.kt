package ru.terrakok.gitlabclient.model.interactor.profile

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import java.util.*

/**
 * @author Artur Badretdinov (Gaket)
 *         31.05.17.
 */
class MyProfileInteractorTest {

    private lateinit var profileRepo: ProfileRepository
    private lateinit var authRepo: AuthRepository

    private lateinit var testUser: User
    private lateinit var testServer: String
    private lateinit var testError: Throwable


    @Before
    fun setUp() {
        testUser = User(1L, null, null, null, null, null,
                null, Date(), false, null, null, null, null, null,
                null, null, Date(), Date(), 0L, 0L, Date(), null, false, false, false, false)
        testServer = "Test server"
        profileRepo = mock(ProfileRepository::class.java)
        authRepo = mock(AuthRepository::class.java)
        testError =  RuntimeException("test error")
    }

    @Test
    fun get_user_info() {
        val testUserInfo = MyUserInfo(testUser, testServer)

        `when`(profileRepo.getMyProfile()).thenReturn(Single.just(testUser))
        `when`(profileRepo.getMyServerName()).thenReturn(testServer)

        `when`(authRepo.getSignState()).thenReturn(Observable.just(true))

        val interactor = MyProfileInteractor(authRepo, profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo).getMyProfile()
        verify(profileRepo).getMyServerName()

        verify(authRepo).getSignState()

        testObserver
                .assertValueCount(1)
                .assertValue(testUserInfo)
                .assertNoErrors()
    }

    @Test
    fun get_unauthorized_user_info() {
        val testUserInfo = MyUserInfo(null, testServer)

        `when`(profileRepo.getMyServerName()).thenReturn(testServer)
        `when`(authRepo.getSignState()).thenReturn(Observable.just(false))

        val interactor = MyProfileInteractor(authRepo, profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo).getMyServerName()
        verify(authRepo).getSignState()

        testObserver
                .assertValueCount(1)
                .assertValue(testUserInfo)
                .assertNoErrors()
    }

    @Test
    fun get_user_info_error() {
        `when`(profileRepo.getMyProfile()).thenReturn(Single.error(testError))
        `when`(profileRepo.getMyServerName()).thenReturn(testServer)

        `when`(authRepo.getSignState()).thenReturn(Observable.just(true))

        val interactor = MyProfileInteractor(authRepo, profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo).getMyProfile()
        testObserver
                .assertNoValues()
                .assertError(testError)
    }

    @Test
    fun check_sign_state_error() {
        `when`(authRepo.getSignState()).thenReturn(Observable.error(testError))

        val interactor = MyProfileInteractor(authRepo, profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verifyNoMoreInteractions(profileRepo)
        testObserver
                .assertNoValues()
                .assertError(testError)
    }
}
package ru.terrakok.gitlabclient.model.interactor.profile

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.user.MyUserInfo
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import java.util.*

/**
 * @author Artur Badretdinov (Gaket)
 *         31.05.17.
 */
class MyProfileInteractorTest {

    private lateinit var profileRepo: ProfileRepository

    private lateinit var testUser: User
    private lateinit var testServer: String
    private lateinit var testError: Throwable


    @Before
    fun setUp() {
        testUser = User(1L, "", null, "", null, null,
                null, Date(), false, null, null, null, null, null,
                null, null, Date(), Date(), 0L, 0L, Date(), null, false, false, false, false)
        testServer = "Test server"
        profileRepo = mock(ProfileRepository::class.java)
        testError =  RuntimeException("test error")
    }

    @Test
    fun get_user_info() {
        val testUserInfo = MyUserInfo(testUser, testServer)

        `when`(profileRepo.getMyProfile()).thenReturn(Single.just(testUser))
        `when`(profileRepo.getMyServerName()).thenReturn(testServer)

        val interactor = MyProfileInteractor(profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo).getMyProfile()
        verify(profileRepo).getMyServerName()

        testObserver
                .assertValueCount(1)
                .assertValue(testUserInfo)
                .assertNoErrors()
    }

    @Test
    fun get_user_info_error() {
        `when`(profileRepo.getMyProfile()).thenReturn(Single.error(testError))
        `when`(profileRepo.getMyServerName()).thenReturn(testServer)

        val interactor = MyProfileInteractor(profileRepo)

        val testObserver: TestObserver<MyUserInfo> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(profileRepo).getMyProfile()
        testObserver
                .assertNoValues()
                .assertError(testError)
    }
}
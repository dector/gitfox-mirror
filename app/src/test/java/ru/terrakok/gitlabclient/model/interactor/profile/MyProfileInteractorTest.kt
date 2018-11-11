package ru.terrakok.gitlabclient.model.interactor.profile

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.threeten.bp.LocalDateTime
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

        val testDate = LocalDateTime.of(2018, 1, 1, 0, 0)

        testUser = User(
            id = 1L,
            username = "",
            email = null,
            name = "",
            state = null,
            avatarUrl = null,
            webUrl = null,
            createdAt = testDate,
            isAdmin = false,
            bio = null,
            location = null,
            skype = null,
            linkedin = null,
            twitter = null,
            websiteUrl = null,
            organization = null,
            lastSignInAt = testDate,
            confirmedAt = testDate,
            colorSchemeId = 0L,
            projectsLimit = 0L,
            currentSignInAt = testDate,
            identities = null,
            canCreateGroup = false,
            canCreateProject = false,
            twoFactorEnabled = false,
            external = false
        )
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
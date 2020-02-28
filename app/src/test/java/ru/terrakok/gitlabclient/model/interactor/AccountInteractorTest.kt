package ru.terrakok.gitlabclient.model.interactor

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Artur Badretdinov (Gaket)
 *         31.05.17.
 *
 * @author Vitaliy Belyaev on 18.05.19.
 *
 */
class AccountInteractorTest {

    private val testServer = "Test server"
    private val testError = RuntimeException("test error")
    private val testDate =
        ZonedDateTime.of(LocalDateTime.of(2018, 1, 1, 0, 0), ZoneOffset.UTC)
    private val testUser = User(
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

    private val api: GitlabApi = mock()
    private val serverChanges: ServerChanges = mock()
    lateinit var interactor: AccountInteractor

    @Before
    fun setup() {
        `when`(serverChanges.issueChanges).thenReturn(Observable.just(0L))
        `when`(serverChanges.mergeRequestChanges).thenReturn(Observable.just(0L))
        `when`(serverChanges.todoChanges).thenReturn(Observable.just(0L))

        interactor = AccountInteractor(
            testServer,
            api,
            serverChanges,
            mock(),
            mock(),
            mock(),
            TestSchedulers()
        )
    }

    @Test
    fun get_user() {
        `when`(api.getMyUser()).thenReturn(Single.just(testUser))

        val testObserver: TestObserver<User> = interactor.getMyProfile().test()
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

        val testObserver: TestObserver<User> = interactor.getMyProfile().test()
        testObserver.awaitTerminalEvent()

        verify(api).getMyUser()

        testObserver
            .assertNoValues()
            .assertError(testError)
    }

    @Test
    fun get_my_server_name() {
        val result = interactor.getMyServerName()
        Assert.assertEquals(testServer, result)
    }
}

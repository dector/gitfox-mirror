package ru.terrakok.gitlabclient.model.interactor.session

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.repository.session.SessionRepository


/**
 * @author Artur Badretdinov (Gaket)
 *        20.12.2016.
 *
 * @author Vitaliy Belyaev on 18.05.2019.
 */
class SessionInteractorTest {

    private lateinit var interactor: SessionInteractor
    private lateinit var repository: SessionRepository
    private lateinit var projectCache: ProjectCache

    private val OAUTH_PARAMS =
            OAuthParams("appId", "appKey", "redirect_url")
    private val testAccount = TestData.getUserAccount()
    
    @Before
    fun setUp() {
        repository = mock()
        projectCache = mock()
        interactor = SessionInteractor(
                "some server path",
                repository,
                OAUTH_PARAMS,
                projectCache
        )
    }

    @Test
    fun check_oauth_redirect() {
        val testUrl = OAUTH_PARAMS.redirectUrl + "somepath"
        val result = interactor.checkOAuthRedirect(testUrl)
        Assert.assertTrue(result)
    }

    @Test
    fun check_oauth_bad_redirect_path() {
        val testUrl = "app://otherUrl/somepath"
        val result = interactor.checkOAuthRedirect(testUrl)
        Assert.assertFalse(result)
    }

    @Test
    fun logout_of_current_user_with_other_account() {
        `when`(repository.getCurrentUserAccount()).thenReturn(testAccount)
        `when`(repository.logout(ArgumentMatchers.anyString())).thenReturn(testAccount)

        val result = interactor.logout()
        verify(repository).getCurrentUserAccount()
        verify(repository).logout(testAccount.id)
        Assert.assertTrue(result)
    }

    @Test
    fun logout_of_current_user_without_other_account() {
        `when`(repository.getCurrentUserAccount()).thenReturn(testAccount)
        `when`(repository.logout(ArgumentMatchers.anyString())).thenReturn(null)

        val result = interactor.logout()
        verify(repository).getCurrentUserAccount()
        verify(repository).logout(testAccount.id)
        Assert.assertFalse(result)
    }

    @Test
    fun logout_of_empty_current_user() {
        `when`(repository.getCurrentUserAccount()).thenReturn(null)

        val result = interactor.logout()
        verify(repository).getCurrentUserAccount()
        Assert.assertFalse(result)
    }

    @Test
    fun logout_of_certain_user_with_other_account() {
        `when`(repository.logout(ArgumentMatchers.anyString())).thenReturn(testAccount)

        val result = interactor.logout(testAccount.id)
        verify(projectCache).clear()
        verify(repository).logout(testAccount.id)
        Assert.assertTrue(result)
    }

    @Test
    fun logout_of_certain_user_without_other_account() {
        `when`(repository.logout(ArgumentMatchers.anyString())).thenReturn(null)

        val result = interactor.logout(testAccount.id)
        verify(projectCache).clear()
        verify(repository).logout(testAccount.id)
        Assert.assertFalse(result)
    }


    @Test
    fun login_through_oauth_with_valid_hash() {
        val code = "helloReader"
        val hash = interactor.oauthUrl.substringAfterLast("=")
        val testUrl = "http://something.com/test?code=$code&state=happiness$hash"

        `when`(repository.login(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(Single.just(testAccount))

        val testObserver: TestObserver<Void> = interactor.login(testUrl).test()
        testObserver.awaitTerminalEvent()

        verify(repository).login(
                OAUTH_PARAMS.appId,
                OAUTH_PARAMS.appKey,
                code,
                OAUTH_PARAMS.redirectUrl)

        testObserver
                .assertNoValues()
                .assertNoErrors()
                .assertComplete()
    }

    @Test
    fun login_through_oauth_with_invalid_hash() {
        val code = "helloReader"
        val hash = "invalidHash"
        val testUrl = "http://something.com/test?code=$code&state=happiness$hash"


        val testObserver: TestObserver<Void> = interactor.login(testUrl).test()
        testObserver.awaitTerminalEvent()

        testObserver
                .assertNoValues()
                .assertError { it is RuntimeException }
                .assertErrorMessage("Not valid oauth hash!")
    }

    @Test
    fun login_through_custom_server_path() {
        val customServerPath = "custom server path"
        val privateToken = "private token"

        `when`(repository.login(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(Single.just(testAccount))

        val testObserver: TestObserver<Void> = interactor.login(customServerPath, privateToken).test()
        testObserver.awaitTerminalEvent()

        verify(repository).login(privateToken, customServerPath)

        testObserver
                .assertNoValues()
                .assertNoErrors()
                .assertComplete()
    }
}
package ru.terrakok.gitlabclient.model.interactor.auth

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.entity.TokenData
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams


/**
 * @author Artur Badretdinov (Gaket)
 *        20.12.2016.
 */
class AuthInteractorTest {

    private lateinit var interactor: AuthInteractor
    private lateinit var authRepo: AuthRepository

    private val HASH = "some_hash_here"
    private val OAUTH_PARAMS =
        OAuthParams("appId", "appKey", "redirect_url")

    @Before
    fun setUp() {
        authRepo = mock()
        interactor = AuthInteractor(
            "some server path",
            "some default server path",
            authRepo,
            HASH,
            OAUTH_PARAMS,
            projectCache = mock()
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
    fun check_logout_cleans_token() {
        interactor.logout()
        verify(authRepo).clearAuthData()
    }

    @Test
    fun is_signed_in() {
        `when`(authRepo.isSignedIn).thenReturn(true)
        val result = interactor.isSignedIn()

        verify(authRepo).isSignedIn
        Assert.assertTrue(result)
    }

    @Test
    fun is_not_signed_in() {
        `when`(authRepo.isSignedIn).thenReturn(false)
        val result = interactor.isSignedIn()

        verify(authRepo).isSignedIn
        Assert.assertFalse(result)
    }

    @Test
    fun refresh_token_correct_oauth() {
        val code = "helloReader"
        val testUrl = "http://something.com/test?code=" + code + "&state=happiness" + HASH
        val tokenData = TokenData("", "", "", 0L, "")

        `when`(authRepo.requestOAuthToken(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(Single.just(tokenData))

        val testObserver: TestObserver<Void> = interactor.login(testUrl).test()
        testObserver.awaitTerminalEvent()

        verify(authRepo).requestOAuthToken(
                OAUTH_PARAMS.appId,
                OAUTH_PARAMS.appKey,
                code,
                OAUTH_PARAMS.redirectUrl)

        testObserver
                .assertNoValues()
                .assertNoErrors()
    }

    @Test
    fun refresh_token_incorrect_oauth() {
        val testOauthRedirect = "There is no token"

        val testObserver: TestObserver<Void> = interactor.login(testOauthRedirect).test()
        testObserver.awaitTerminalEvent()

        verifyNoMoreInteractions(authRepo)

        testObserver
                .assertNoValues()
                .assertError(RuntimeException::class.java)
    }
}
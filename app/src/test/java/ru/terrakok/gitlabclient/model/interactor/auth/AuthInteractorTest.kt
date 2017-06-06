package ru.terrakok.gitlabclient.model.interactor.auth

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository

/**
 * @author Artur Badretdinov (Gaket)
 *        20.12.2016.
 */
class AuthInteractorTest {

    private lateinit var interactor: AuthInteractor
    private lateinit var authRepo: AuthRepository

    private val REDIRECT_URI = "app://url/"
    private val HASH = "some_hash_here"
    private val EMPTY_STRING = ""

    @Before
    fun setUp() {
        authRepo = mock(AuthRepository::class.java)
        `when`(authRepo.getSignState()).thenReturn(Observable.just(true))

        val serverConf = Mockito.mock(ServerConfig::class.java)
        `when`(serverConf.AUTH_REDIRECT_URI).thenReturn(REDIRECT_URI)
        `when`(serverConf.APP_ID).thenReturn(EMPTY_STRING)
        `when`(serverConf.APP_KEY).thenReturn(EMPTY_STRING)
        `when`(serverConf.SERVER_URL).thenReturn(EMPTY_STRING)

        interactor = AuthInteractor(serverConf, authRepo, HASH)
    }

    @Test
    fun check_oauth_redirect() {
        val testUrl = REDIRECT_URI + "somepath"
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
        `when`(authRepo.clearToken()).thenReturn(Completable.complete())

        val testObserver: TestObserver<Void> = interactor.logout().test()
        testObserver.awaitTerminalEvent()

        verify(authRepo).clearToken()
        testObserver
                .assertNoErrors()
                .assertNoValues()
    }

    @Test
    fun is_signed_in() {
        `when`(authRepo.getSignState()).thenReturn(Observable.just(true, false))

        val testObserver: TestObserver<Boolean> = interactor.isSignedIn().test()
        testObserver.awaitTerminalEvent()

        verify(authRepo).getSignState()
        testObserver
                .assertNoErrors()
                .assertValueCount(1)
                .assertValue(true)
    }

    @Test
    fun is_signed_in_error() {
        val error = RuntimeException("test error")

        `when`(authRepo.getSignState()).thenReturn(Observable.error(error))

        val testObserver: TestObserver<Boolean> = interactor.isSignedIn().test()
        testObserver.awaitTerminalEvent()

        verify(authRepo).getSignState()
        testObserver
                .assertError(error)
                .assertNoValues()
    }

    @Test
    fun is_signed_in_no_values() {
        `when`(authRepo.getSignState()).thenReturn(Observable.empty())

        val testObserver: TestObserver<Boolean> = interactor.isSignedIn().test()
        testObserver.awaitTerminalEvent()

        verify(authRepo).getSignState()
        testObserver
                .assertNoValues()
                .assertError(Throwable::class.java)
    }

    @Test
    fun refresh_token_correct_oauth() {
        val code = "helloReader"
        val testUrl = "http://something.com/test?code=" + code + "&state=happiness" + HASH

        `when`(authRepo.refreshServerToken(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(Completable.complete())

        val testObserver: TestObserver<Void> = interactor.login(testUrl).test()
        testObserver.awaitTerminalEvent()

        verify(authRepo).refreshServerToken(
                EMPTY_STRING,
                EMPTY_STRING,
                code,
                REDIRECT_URI)

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
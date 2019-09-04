package ru.terrakok.gitlabclient.model.interactor

import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import ru.terrakok.gitlabclient.TestData
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.model.data.storage.Prefs

/**
 * @author Vitaliy Belyaev on 08.06.2019.
 */
class SessionInteractorTest {
    private val testAccount = TestData.getUserAccount()
    private val oauthParams = OAuthParams(
        "https://someurl.com",
        "appId",
        "appKey",
        "redirect_url"
    )
    private val prefs = mock(Prefs::class.java)
    private val api: UserAccountApi = mock()
    private val projectCache: ProjectCache = mock()

    private val interactor = SessionInteractor(
        prefs,
        oauthParams,
        api,
        projectCache,
        TestSchedulers()
    )

    @Test
    fun `get current account should return null when there is no selected account`() {
        // GIVEN
        given(prefs.selectedAccount).willReturn(null)

        // WHEN
        val result = interactor.getCurrentUserAccount()

        // THEN
        then(prefs).should(times(1)).selectedAccount
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(null, result)
    }

    @Test
    fun `get current account should return stored selected account`() {
        // GIVEN
        given(prefs.selectedAccount).willReturn(testAccount.id)
        given(prefs.accounts).willReturn(listOf(testAccount, testAccount.copy(userId = 43)))

        // WHEN
        val result = interactor.getCurrentUserAccount()

        // THEN
        then(prefs).should(times(1)).selectedAccount
        then(prefs).should(times(1)).accounts
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(testAccount, result)
    }

    @Test
    fun `set current account should return null when not find account with such id`() {
        // GIVEN
        given(prefs.accounts).willReturn(listOf(testAccount.copy(userId = 43)))

        // WHEN
        val result = interactor.setCurrentUserAccount(testAccount.id)

        // THEN
        then(prefs).should(times(1)).accounts
        then(prefs).should(times(1)).selectedAccount = null
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(null, result)
    }

    @Test
    fun `set current account should return stored account and set selected account`() {
        // GIVEN
        given(prefs.accounts).willReturn(listOf(testAccount))

        // WHEN
        val result = interactor.setCurrentUserAccount(testAccount.id)

        // THEN
        then(prefs).should(times(1)).accounts
        then(prefs).should(times(1)).selectedAccount = testAccount.id
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(testAccount, result)
    }

    @Test
    fun `get user accounts should return stored user accounts`() {
        // GIVEN
        val storedAccounts = listOf(testAccount, testAccount.copy(userId = 43))

        given(prefs.accounts).willReturn(storedAccounts)

        // WHEN
        val result = interactor.getUserAccounts()

        // THEN
        then(prefs).should(times(1)).accounts
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(storedAccounts, result)
    }

    @Test
    fun `logout should remove account from stored user accounts`() {
        // GIVEN
        val storedAccounts = listOf(testAccount, testAccount.copy(userId = 43))
        val expectedAccounts = listOf(testAccount.copy(userId = 43))

        given(prefs.accounts).willReturn(storedAccounts)

        // WHEN
        interactor.logoutFromAccount(testAccount.id)

        // THEN
        then(prefs).should(times(1)).accounts
        then(prefs).should(times(1)).accounts = expectedAccounts
    }

    @Test
    fun `logout should return first account from new stored accounts when ids matches`() {
        // GIVEN
        val storedAccounts = listOf(testAccount, testAccount.copy(userId = 43))
        val expectedAccounts = listOf(testAccount.copy(userId = 43))
        val newAccount = testAccount.copy(userId = 43)

        given(prefs.accounts).willReturn(storedAccounts)
        given(prefs.selectedAccount).willReturn(testAccount.id)

        // WHEN
        val result = interactor.logoutFromAccount(testAccount.id)

        // THEN
        then(prefs).should(times(1)).accounts
        then(prefs).should(times(1)).accounts = expectedAccounts
        then(prefs).should(times(1)).selectedAccount
        then(prefs).should(times(1)).selectedAccount = newAccount.id
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(newAccount, result)
    }

    @Test
    fun `logout should return selected account from new stored accounts when ids not matches`() {
        // GIVEN
        val storedAccounts = listOf(testAccount, testAccount.copy(userId = 43))
        val expectedAccounts = listOf(testAccount.copy(userId = 43))
        val selectedAccount = testAccount.copy(userId = 43)

        given(prefs.accounts).willReturn(storedAccounts)
        given(prefs.selectedAccount).willReturn(selectedAccount.id)

        // WHEN
        val result = interactor.logoutFromAccount(testAccount.id)

        // THEN
        then(prefs).should(times(1)).accounts
        then(prefs).should(times(1)).accounts = expectedAccounts
        then(prefs).should(times(1)).selectedAccount
        then(prefs).shouldHaveNoMoreInteractions()

        Assert.assertEquals(selectedAccount, result)
    }
}
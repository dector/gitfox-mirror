package ru.terrakok.gitlabclient.model.interactor

import gitfox.entity.app.develop.AppInfo
import gitfox.entity.app.develop.AppLibrary
import gitfox.entity.app.develop.LicenseType
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.model.data.storage.RawAppData

/**
 * @author Vitaliy Belyaev on 21.05.2019.
 */
class AppInfoInteractorTest {

    private val appInfo = AppInfo(
            "1.5.1",
            13,
            "test description",
            "wer342frvr3",
            "https://gitlab.com/terrakok/gitlab-client",
            "https://gitlab.com/terrakok/gitlab-client/issues"
    )
    private val testAppLibraries = listOf(AppLibrary("name", "url", LicenseType.MIT))

    private val rawAppData = mock(RawAppData::class.java)
    private val interactor = AppInfoInteractor(
        rawAppData,
        appInfo,
        TestSchedulers()
    )

    @Test
    fun `get app info success`() {
        val testObserver = interactor.getAppInfo().test()

        testObserver
                .assertNoErrors()
                .assertValue(appInfo)
                .assertComplete()
    }

    @Test
    fun `get app libraries success`() {
        `when`(rawAppData.getAppLibraries()).thenReturn(Single.just(testAppLibraries))

        val testObserver = interactor.getAppLibraries().test()

        verify(rawAppData, times(1)).getAppLibraries()
        verifyNoMoreInteractions(rawAppData)

        testObserver
                .assertNoErrors()
                .assertValue(testAppLibraries)
                .assertComplete()
    }

    @Test
    fun `get empty libraries list on error`() {
        `when`(rawAppData.getAppLibraries()).thenReturn(Single.error(RuntimeException()))

        val testObserver = interactor.getAppLibraries().test()

        verify(rawAppData, times(1)).getAppLibraries()
        verifyNoMoreInteractions(rawAppData)

        testObserver
                .assertNoErrors()
                .assertValue(emptyList())
                .assertComplete()
    }
}

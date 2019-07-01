package ru.terrakok.gitlabclient.model.repository.app

import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*
import ru.terrakok.gitlabclient.TestSchedulers
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.entity.app.develop.LicenseType
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.data.storage.RawAppData

/**
 * @author Vitaliy Belyaev on 21.05.2019.
 */
class AppInfoRepositoryTest {

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
    private val prefs = mock(Prefs::class.java)
    private val repository = AppInfoRepository(rawAppData, appInfo, prefs, TestSchedulers())

    @Test
    fun `get timestamp success`() {
        val timestamp = 1234545454L

        `when`(prefs.firstLaunchTimeStamp).thenReturn(timestamp)

        val result = repository.firstLaunchTimeStamp

        verify(prefs, times(1)).firstLaunchTimeStamp
        verifyNoMoreInteractions(prefs)

        assertEquals(timestamp, result)
    }

    @Test
    fun `set timestamp success`() {
        val timestamp = 1234545454L

        repository.firstLaunchTimeStamp = timestamp

        verify(prefs, times(1)).firstLaunchTimeStamp = timestamp
        verifyNoMoreInteractions(prefs)
    }

    @Test
    fun `get app info success`() {
        val testObserver = repository.getAppInfo().test()

        testObserver
                .assertNoErrors()
                .assertValue(appInfo)
                .assertComplete()
    }

    @Test
    fun `get app libraries success`() {
        `when`(rawAppData.getAppLibraries()).thenReturn(Single.just(testAppLibraries))

        val testObserver = repository.getAppLibraries().test()

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

        val testObserver = repository.getAppLibraries().test()

        verify(rawAppData, times(1)).getAppLibraries()
        verifyNoMoreInteractions(rawAppData)

        testObserver
                .assertNoErrors()
                .assertValue(emptyList())
                .assertComplete()
    }
}
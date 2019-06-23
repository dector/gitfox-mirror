package ru.terrakok.gitlabclient.presentation.global

import com.nhaarman.mockitokotlin2.*
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.TestSchedulers
import java.util.concurrent.CountDownLatch

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 *
 * @author Vitaliy Belyaev on 19.05.2019.
 */
class PaginatorTest {

    private val render: (Paginator.State<Any>) -> Unit = mock()
    private val dataSource: (Int) -> List<Any> = mock()

    private lateinit var captor: KArgumentCaptor<Paginator.State<Any>>
    private lateinit var paginator: Paginator.Store<Any>

    private var disposable: Disposable? = null

    private val initState = Paginator.State<Any>(0, false, emptyList(), false, null)
    private val emptyProgressState = Paginator.State<Any>(0, true, emptyList(), false, null)

    @Before
    fun setUp() {
        disposable?.dispose()
        reset(render)
        reset(dataSource)

        captor = argumentCaptor()

        paginator = Paginator.Store(TestSchedulers())
        paginator.render = render
        disposable = paginator.sideEffects.subscribe { se ->
            when (se) {
                is Paginator.SideEffect.LoadPage ->
                    paginator.proceed(
                        try {
                            val page = dataSource(se.currentPage)
                            Paginator.Action.NewPage(se.currentPage, page)
                        } catch (e: Throwable) {
                            Paginator.Action.PageError(e)
                        }
                    )
            }
        }
    }

    @Test
    fun `load empty list`() {
        val data = emptyList<Any>()
        whenever(dataSource(any())).thenReturn(data)

        paginator.proceed(Paginator.Action.Refresh)

        verify(render, times(3)).invoke(captor.capture())
        assert(captor.allValues[0] == initState)
        assert(captor.allValues[1] == emptyProgressState)
        assert(captor.allValues[2] == Paginator.State(1, false, data, false, null))
    }

    @Test
    fun `load first page`() {
        val data = listOf(Any())
        whenever(dataSource(any())).thenReturn(data)

        paginator.proceed(Paginator.Action.Refresh)

        verify(render, times(3)).invoke(captor.capture())
        assert(captor.allValues[0] == initState)
        assert(captor.allValues[1] == emptyProgressState)
        assert(captor.allValues[2] == Paginator.State(1, false, data, false, null))
    }

    @Test
    fun `load first page with error`() {
        val error = RuntimeException("error")
        whenever(dataSource(any())).thenThrow(error)

        paginator.proceed(Paginator.Action.Refresh)

        verify(render, times(3)).invoke(captor.capture())
        assert(captor.allValues[0] == initState)
        assert(captor.allValues[1] == emptyProgressState)
        assert(captor.allValues[2] == Paginator.State(0, false, emptyList<Any>(), false, error))
    }

    @Test
    fun `load multiple pages`() {
        val page1 = listOf(Any())
        val page2 = listOf(Any())
        val firstPageState = Paginator.State(1, false, page1, false, null)

        val lock = CountDownLatch(1)
        whenever(dataSource(any()))
            .thenReturn(page1)
            .thenReturn(page2)
        whenever(render(firstPageState)).then { lock.countDown() }

        paginator.proceed(Paginator.Action.Refresh)
        lock.await()
        paginator.proceed(Paginator.Action.LoadMore)

        verify(render, times(5)).invoke(captor.capture())
        assert(captor.allValues[0] == initState)
        assert(captor.allValues[1] == emptyProgressState)
        assert(captor.allValues[2] == firstPageState)
        assert(captor.allValues[3] == Paginator.State(1, false, page1, true, null))
        assert(captor.allValues[4] == Paginator.State(2, false, page1 + page2, false, null))
    }

    @Test
    fun `load multiple pages with error on second page`() {
        val page1 = listOf(Any())
        val error = RuntimeException("error")
        val firstPageState = Paginator.State(1, false, page1, false, null)

        val lock = CountDownLatch(1)
        whenever(dataSource(any()))
            .thenReturn(page1)
            .thenThrow(error)
        whenever(render(firstPageState)).then { lock.countDown() }

        paginator.proceed(Paginator.Action.Refresh)
        lock.await()
        paginator.proceed(Paginator.Action.LoadMore)

        verify(render, times(5)).invoke(captor.capture())
        assert(captor.allValues[0] == initState)
        assert(captor.allValues[1] == emptyProgressState)
        assert(captor.allValues[2] == firstPageState)
        assert(captor.allValues[3] == Paginator.State(1, false, page1, true, null))
        assert(captor.allValues[4] == Paginator.State(1, false, page1, false, error))
    }
}
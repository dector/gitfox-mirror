package ru.terrakok.gitlabclient.presentation.global

import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import ru.terrakok.gitlabclient.TestSchedulers

class PaginatorTest {

    private val render: (Paginator.State) -> Unit = mock()
    private val dataSource: (Int) -> List<Any> = mock()
    private val errorRender: (Throwable) -> Unit = mock()

    private lateinit var captor: KArgumentCaptor<Paginator.State>
    private lateinit var paginator: Paginator.Store<Any>

    @Before
    fun setUp() {
        captor = argumentCaptor()

        paginator = Paginator.Store(TestSchedulers())
        paginator.render = render
        paginator.sideEffects.subscribe { se ->
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
                is Paginator.SideEffect.ErrorEvent ->
                    errorRender(se.error)
            }
        }
    }

    @Test
    fun `load empty list`() {
        whenever(dataSource(any())).thenReturn(emptyList())

        paginator.proceed(Paginator.Action.Refresh)
        Thread.sleep(100)

        verify(render, times(3)).invoke(captor.capture())
        assert(captor.allValues[0] == Paginator.State.Empty)
        assert(captor.allValues[1] == Paginator.State.EmptyProgress)
        assert(captor.allValues[2] == Paginator.State.Empty)
    }

    @Test
    fun `load first page`() {
        val data = listOf(Any())
        whenever(dataSource(any())).thenReturn(data)

        paginator.proceed(Paginator.Action.Refresh)
        Thread.sleep(100)

        verify(render, times(3)).invoke(captor.capture())
        assert(captor.allValues[0] == Paginator.State.Empty)
        assert(captor.allValues[1] == Paginator.State.EmptyProgress)
        assert(captor.allValues[2] == Paginator.State.Data(1, data))
    }

    @Test
    fun `load first page with error`() {
        val error = RuntimeException("error")
        whenever(dataSource(any())).thenThrow(error)

        paginator.proceed(Paginator.Action.Refresh)
        Thread.sleep(100)

        verify(render, times(3)).invoke(captor.capture())
        assert(captor.allValues[0] == Paginator.State.Empty)
        assert(captor.allValues[1] == Paginator.State.EmptyProgress)
        assert(captor.allValues[2] == Paginator.State.EmptyError(error))
    }

//    @Test
//    fun `load multiple pages`() {
//        val page1 = listOf(Any())
//        val page2 = listOf(Any(), Any())
//        val firstPageState = Paginator.State.Data(1, page1)
//        val secondPageState = Paginator.State.Data(2, page1 + page2)
//
//        val lock = CountDownLatch(1)
//        val finishLock = CountDownLatch(1)
//        whenever(dataSource(any()))
//            .thenReturn(page1)
//            .thenReturn(page2)
//        whenever(render(firstPageState)).then { lock.countDown() }
//        whenever(render(secondPageState)).then { finishLock.countDown() }
//
//        paginator.proceed(Paginator.Action.Refresh)
//        lock.await()
//        paginator.proceed(Paginator.Action.LoadMore)
//        finishLock.await(100, TimeUnit.MILLISECONDS)
//
//        verify(render, times(5)).invoke(captor.capture())
//        assert(captor.allValues[0] == Paginator.State.Empty)
//        assert(captor.allValues[1] == Paginator.State.EmptyProgress)
//        assert(captor.allValues[2] == firstPageState)
//        assert(captor.allValues[3] == Paginator.State.NewPageProgress(1, page1))
//        assert(captor.allValues[4] == secondPageState)
//    }

//    @Test
//    fun `load multiple pages with error on second page`() {
//        val page1 = listOf(Any())
//        val error = RuntimeException("error")
//        val firstPageState = Paginator.State.Data(1, page1)
//
//        val lock = CountDownLatch(1)
//        val finishLock = CountDownLatch(1)
//        whenever(dataSource(any()))
//            .thenReturn(page1)
//            .thenThrow(error)
//        whenever(render(firstPageState))
//            .then { lock.countDown() }
//            .then { finishLock.countDown() }
//
//        paginator.proceed(Paginator.Action.Refresh)
//        lock.await()
//        paginator.proceed(Paginator.Action.LoadMore)
//        finishLock.await(100, TimeUnit.MILLISECONDS)
//
//        verify(render, times(5)).invoke(captor.capture())
//        assert(captor.allValues[0] == Paginator.State.Empty)
//        assert(captor.allValues[1] == Paginator.State.EmptyProgress)
//        assert(captor.allValues[2] == firstPageState)
//        assert(captor.allValues[3] == Paginator.State.NewPageProgress(1, page1))
//        assert(captor.allValues[4] == firstPageState)
//
//        verify(errorRender).invoke(error)
//    }
}

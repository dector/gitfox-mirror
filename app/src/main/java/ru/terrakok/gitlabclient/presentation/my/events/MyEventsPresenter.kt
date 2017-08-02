package ru.terrakok.gitlabclient.presentation.my.events

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class MyEventsPresenter @Inject constructor(
        private val eventInteractor: EventInteractor,
        private val resourceManager: ResourceManager,
        private val menuController: GlobalMenuController
) : MvpPresenter<MyEventsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshEvents()
    }

    private val paginator = Paginator(
            { eventInteractor.getEvents(it) },
            object : Paginator.ViewController<Event> {
                override fun showEmptyProgress(show: Boolean) {
                    viewState.showEmptyProgress(show)
                }

                override fun showEmptyError(show: Boolean, error: Throwable?) {
                    viewState.showEmptyError(show, error?.userMessage(resourceManager))
                }

                override fun showErrorMessage(error: Throwable) {
                    viewState.showMessage(error.userMessage(resourceManager))
                }

                override fun showEmptyView(show: Boolean) {
                    viewState.showEmptyView(show)
                }

                override fun showData(show: Boolean, data: List<Event>) {
                    viewState.showEvents(show, data)
                }

                override fun showRefreshProgress(show: Boolean) {
                    viewState.showRefreshProgress(show)
                }

                override fun showPageProgress(show: Boolean) {
                    viewState.showPageProgress(show)
                }
            }
    )

    fun onMenuClick() = menuController.open()
    fun onEventClick(event: Event) {}
    fun refreshEvents() = paginator.refresh()
    fun loadNextEventsPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}
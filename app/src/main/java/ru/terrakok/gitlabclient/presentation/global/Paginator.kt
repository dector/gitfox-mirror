package ru.terrakok.gitlabclient.presentation.global

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 2019-06-21.
 */
object Paginator {
    data class State<T>(
        val pageCount: Int,
        val refreshProgress: Boolean,
        val items: List<T>,
        val pageProgress: Boolean,
        val error: Throwable?
    )

    sealed class Action {
        object Refresh : Action()
        object Restart : Action()
        object LoadMore : Action()
        data class NewPage<T>(val pageNumber: Int, val items: List<T>) : Action()
        data class PageError(val error: Throwable) : Action()
    }

    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
    }

    private fun <T> reducer(action: Action, state: State<T>, sideEffectListener: (SideEffect) -> Unit): State<T> =
        when (action) {
            is Action.Refresh -> {
                if (state.refreshProgress) {
                    state
                } else {
                    sideEffectListener(SideEffect.LoadPage(1))
                    state.copy(refreshProgress = true, pageProgress = false, error = null)
                }
            }
            is Action.Restart -> {
                sideEffectListener(SideEffect.LoadPage(1))
                State(0, true, emptyList(), false, null)
            }
            is Action.LoadMore -> {
                if (state.refreshProgress || state.pageProgress) {
                    state
                } else {
                    sideEffectListener(SideEffect.LoadPage(state.pageCount + 1))
                    state.copy(pageProgress = true)
                }
            }
            is Action.NewPage<*> -> {
                action.items as List<T>
                when {
                    state.refreshProgress -> State(1, false, action.items, false, null)
                    state.pageProgress -> State(action.pageNumber, false, state.items + action.items, false, null)
                    else -> state
                }
            }
            is Action.PageError -> {
                state.copy(refreshProgress = false, pageProgress = false, error = action.error)
            }
        }

    class Store<T> @Inject constructor(schedulers: SchedulersProvider) {
        private var state: State<T> = State(0, false, emptyList(), false, null)
        var render: (State<T>) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }

        private val sideEffectsExecutor = Executors.newSingleThreadExecutor()
        private val sideEffectRelay = PublishRelay.create<SideEffect>()
        val sideEffects: Observable<SideEffect> =
            sideEffectRelay
                .hide()
                .observeOn(schedulers.ui())

        fun proceed(action: Action) {
            Timber.d("Action: $action")
            val newState = reducer(action, state) { sideEffect ->
                sideEffectsExecutor.submit { sideEffectRelay.accept(sideEffect) }
            }
            if (newState != state) {
                state = newState
                Timber.d("New state: $state")
                render(state)
            }
        }
    }
}
package ru.terrakok.gitlabclient.presentation.global

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 2019-06-21.
 */
object Paginator {
    data class State<T>(
        val refreshProgress: Boolean,
        val items: List<T>,
        val pageProgress: Boolean,
        val error: Throwable?
    )

    sealed class Action {
        object Refresh : Action()
        object Restart : Action()
        object LoadMore : Action()
        data class NewPage<T>(val items: List<T>) : Action()
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
                    sideEffectListener(SideEffect.LoadPage(0))
                    State(true, state.items, false, state.error)
                }
            }
            is Action.Restart -> {
                sideEffectListener(SideEffect.LoadPage(0))
                State(true, emptyList(), false, null)
            }
            is Action.LoadMore -> {
                if (state.refreshProgress || state.pageProgress) {
                    state
                } else {
                    sideEffectListener(SideEffect.LoadPage(state.items.size / 20))
                    State(false, state.items, true, state.error)
                }
            }
            is Action.NewPage<*> -> {
                action.items as List<T>
                when {
                    state.refreshProgress -> State(false, action.items, false, null)
                    state.pageProgress -> State(false, state.items + action.items, false, null)
                    else -> state
                }
            }
            is Action.PageError -> {
                State(false, state.items, false, action.error)
            }
        }

    class Store<T> {
        var render: (State<T>) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }
        var sideEffectListener: (SideEffect) -> Unit = {}

        private var state: State<T> = State(false, emptyList(), false, null)

        fun proceed(action: Action) {
            state = reducer(action, state, sideEffectListener)
            render(state)
        }
    }
}
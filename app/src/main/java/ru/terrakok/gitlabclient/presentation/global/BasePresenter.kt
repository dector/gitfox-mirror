package ru.terrakok.gitlabclient.presentation.global

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import moxy.MvpPresenter
import moxy.MvpView

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 07.01.18.
 */
open class BasePresenter<V : MvpView> :
    MvpPresenter<V>(),
    CoroutineScope by CoroutineScope(Dispatchers.Main) {

    override fun onDestroy() {
        cancel()
    }
}

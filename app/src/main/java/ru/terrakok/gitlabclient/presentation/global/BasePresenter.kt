package ru.terrakok.gitlabclient.presentation.global

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
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

    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        cancel()
        compositeDisposable.dispose()
    }

    protected fun Disposable.connect() {
        compositeDisposable.add(this)
    }
}

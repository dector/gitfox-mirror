package ru.terrakok.gitlabclient.presentation.global

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ListMvpView<T> {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showItems(show: Boolean, list: List<T>)
}
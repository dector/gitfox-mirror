package ru.terrakok.gitlabclient.presentation.my.todos

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.09.17
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MyTodoListView : MvpView {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showTodos(show: Boolean, todos: List<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
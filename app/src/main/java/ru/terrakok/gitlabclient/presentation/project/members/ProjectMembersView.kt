package ru.terrakok.gitlabclient.presentation.project.members

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Member

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectMembersView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showMembers(show: Boolean, members: List<Member>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
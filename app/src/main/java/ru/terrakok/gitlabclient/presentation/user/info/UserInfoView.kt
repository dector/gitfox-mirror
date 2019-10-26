package ru.terrakok.gitlabclient.presentation.user.info

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.User

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface UserInfoView : MvpView {
    fun showUser(user: User)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(msg: String)
}
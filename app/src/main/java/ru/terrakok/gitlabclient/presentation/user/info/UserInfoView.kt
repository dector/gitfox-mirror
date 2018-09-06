package ru.terrakok.gitlabclient.presentation.user.info

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
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
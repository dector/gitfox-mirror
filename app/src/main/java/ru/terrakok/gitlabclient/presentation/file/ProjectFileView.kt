package ru.terrakok.gitlabclient.presentation.file

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectFileView : MvpView
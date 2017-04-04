package ru.terrakok.gitlabclient.mvp.drawer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.gitlabclient.BuildConfig

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter : MvpPresenter<NavigationDrawerView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.showVersionName("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }
}
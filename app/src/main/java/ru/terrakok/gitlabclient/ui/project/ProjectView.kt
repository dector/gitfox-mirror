package ru.terrakok.gitlabclient.ui.project

import com.arellomobile.mvp.MvpView

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
interface ProjectView : MvpView {

    fun setProjectName(name: String)
}
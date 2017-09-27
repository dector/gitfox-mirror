package ru.terrakok.gitlabclient.presentation.projects

import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class ProjectsContainerPresenter @Inject constructor(
        private val router: Router
) : MvpPresenter<ProjectsConteinerView>() {

    fun onBackPressed() = router.exit()
}
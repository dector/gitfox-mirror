package ru.terrakok.gitlabclient.model.interactor.utils

import io.reactivex.Single
import ru.terrakok.gitlabclient.model.repository.tools.MarkDownConverter
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.01.18.
 */
class UtilsInteractor @Inject constructor(
        private val mdConverter: MarkDownConverter,
        private val schedulers: SchedulersProvider
) {

    fun md2html(md: String) = Single
            .fromCallable { mdConverter.markdownToHtml(md) }
            .subscribeOn(schedulers.computation())
            .observeOn(schedulers.ui())
}
package ru.terrakok.gitlabclient

import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
class TestSchedulers : SchedulersProvider {
    override fun ui() = Schedulers.trampoline()
    override fun computation() = Schedulers.trampoline()
    override fun trampoline() = Schedulers.trampoline()
    override fun newThread() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
}
package ru.terrakok.gitlabclient.model.system

import io.reactivex.Scheduler

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */
interface SchedulersProvider {
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun trampoline(): Scheduler
    fun newThread(): Scheduler
    fun io(): Scheduler
}

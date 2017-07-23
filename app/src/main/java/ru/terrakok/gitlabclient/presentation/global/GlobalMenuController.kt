package ru.terrakok.gitlabclient.presentation.global

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.07.17.
 */
class GlobalMenuController {
    private val stateRelay = BehaviorRelay.createDefault(false)

    val state: Observable<Boolean> = stateRelay
    fun open() = stateRelay.accept(true)
    fun close() = stateRelay.accept(false)
}
package ru.terrakok.gitlabclient.model.system.flow

import ru.terrakok.cicerone.Router

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 19.12.17.
 */
class FlowRouter : Router() {

    fun startFlow(flowKey: String, data: Any? = null) {
        executeCommands(StartFlow(flowKey, data))
    }

    fun finishFlow(data: Any? = null) {
        executeCommands(FinishFlow(data))
    }

    fun cancelFlow() {
        finishChain()
    }
}
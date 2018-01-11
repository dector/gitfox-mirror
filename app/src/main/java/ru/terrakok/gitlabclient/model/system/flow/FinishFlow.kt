package ru.terrakok.gitlabclient.model.system.flow

import ru.terrakok.cicerone.commands.Command

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 10.01.18.
 */
class FinishFlow(
        val transitionData: Any?
) : Command
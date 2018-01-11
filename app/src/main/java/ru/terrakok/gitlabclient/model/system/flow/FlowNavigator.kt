package ru.terrakok.gitlabclient.model.system.flow

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 10.01.18.
 */
abstract class FlowNavigator(
        val activity: FragmentActivity,
        containerId: Int
) : SupportFragmentNavigator(activity.supportFragmentManager, containerId) {

    fun setLaunchScreen(screenKey: String, data: Any? = null) {
        applyCommands(arrayOf(
                BackTo(null),
                Replace(screenKey, data)
        ))
    }

    override fun applyCommand(command: Command?) {
        when (command) {
            is StartFlow -> startFlow(command.screenKey, command.transitionData)
            is FinishFlow -> finishFlow(command.transitionData)
            else -> super.applyCommand(command)
        }
    }

    private fun startFlow(flowKey: String, data: Any?) {
        createFlowIntent(flowKey, data)?.let { intent ->
            activity.startActivityForResult(intent, getRequestCodeForFlow(flowKey))
        }
    }

    private fun finishFlow(data: Any?) {
        activity.setResult(Activity.RESULT_OK, createFlowResult(data))
        activity.finish()
    }

    open fun createFlowIntent(flowKey: String, data: Any?): Intent? = null

    open fun getRequestCodeForFlow(flowKey: String): Int = -1

    open fun createFlowResult(data: Any?): Intent? = null

    override fun exit() {
        activity.setResult(Activity.RESULT_CANCELED)
        activity.finish()
    }

    override fun showSystemMessage(message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
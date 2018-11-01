package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.terrakok.gitlabclient.App

private const val PROGRESS_TAG = "bf_progress"
private const val STATE_LAUNCH_FLAG = "state_launch_flag"

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseFragment : MvpAppCompatFragment() {
    abstract val layoutRes: Int

    private var instanceStateSaved: Boolean = false

    private val viewHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutRes, container, false)

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    //fix for async views (like swipeToRefresh and RecyclerView)
    //if synchronously call actions on swipeToRefresh in sequence show and hide then swipeToRefresh will not hidden
    protected fun postViewAction(action: () -> Unit) {
        viewHandler.post(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewHandler.removeCallbacksAndMessages(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_LAUNCH_FLAG, App.appCode)
        instanceStateSaved = true
    }

    protected fun showProgressDialog(progress: Boolean) {
        if (!isAdded || instanceStateSaved) return

        val fragment = childFragmentManager.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !progress) {
            (fragment as ProgressDialog).dismissAllowingStateLoss()
            childFragmentManager.executePendingTransactions()
        } else if (fragment == null && progress) {
            ProgressDialog().show(childFragmentManager, PROGRESS_TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    protected fun isFirstLaunch(savedInstanceState: Bundle?): Boolean {
        val savedAppHashCode = savedInstanceState?.getString(STATE_LAUNCH_FLAG)
        return savedAppHashCode != App.appCode
    }

    open fun onBackPressed() {}
}
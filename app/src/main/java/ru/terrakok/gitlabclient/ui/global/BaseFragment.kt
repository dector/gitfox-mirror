package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.extension.objectScopeName
import timber.log.Timber
import toothpick.Scope
import toothpick.Toothpick

private const val PROGRESS_TAG = "bf_progress"
private const val STATE_SCOPE_NAME = "state_scope_name"
private const val STATE_LAUNCH_FLAG = "state_launch_flag"
private const val STATE_SCOPE_WAS_CLOSED = "state_scope_was_closed"

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseFragment : MvpAppCompatFragment() {
    abstract val layoutRes: Int

    private var instanceStateSaved: Boolean = false

    private val viewHandler = Handler()

    protected open val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment)?.fragmentScopeName
            ?: throw RuntimeException("Must be parent fragment!")
    }

    protected open val scopeModuleInstaller: (Scope) -> Unit = {}

    private lateinit var fragmentScopeName: String
    protected lateinit var scope: Scope
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedAppCode = savedInstanceState?.getString(STATE_LAUNCH_FLAG)
        //False - if fragment was restored without new app process (for example: activity rotation)
        val isNewInAppProcess = savedAppCode != App.appCode
        val scopeWasClosed = savedInstanceState?.getBoolean(STATE_SCOPE_WAS_CLOSED) ?: true

        val scopeIsNotInit = isNewInAppProcess || scopeWasClosed
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME) ?: objectScopeName()
        scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            .apply {
                if (scopeIsNotInit) {
                    Timber.d("Init new UI scope: $fragmentScopeName")
                    scopeModuleInstaller(this)
                } else {
                    Timber.d("Get exist UI scope: $fragmentScopeName")
                }
            }

        super.onCreate(savedInstanceState)
    }

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
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
        outState.putString(STATE_LAUNCH_FLAG, App.appCode)
        outState.putBoolean(STATE_SCOPE_WAS_CLOSED, needCloseScope()) //save it but will be used only if destroyed
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            //destroy this fragment with scope
            Timber.d("Destroy UI scope: $fragmentScopeName")
            Toothpick.closeScope(scope.name)
        }
    }

    //This is android, baby!
    private fun isRealRemoving(): Boolean =
        (isRemoving && !instanceStateSaved) //because isRemoving == true for fragment in backstack on screen rotation
            || ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    //It will be valid only for 'onDestroy()' method
    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
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

    open fun onBackPressed() {}
}
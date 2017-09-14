package ru.terrakok.gitlabclient.ui.global

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseFragment : MvpAppCompatFragment() {
    companion object {
        private val PROGRESS_TAG = "bf_progress"
    }

    abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(layoutRes, container, false)

    protected fun showProgressDialog(progress: Boolean) {
        val fragment = childFragmentManager?.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !progress) {
            (fragment as ProgressDialog).dismissAllowingStateLoss()
            childFragmentManager.executePendingTransactions()
        } else if (fragment == null && progress) {
            ProgressDialog().show(childFragmentManager, PROGRESS_TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    protected fun showSnackMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    open fun onBackPressed() {}
}
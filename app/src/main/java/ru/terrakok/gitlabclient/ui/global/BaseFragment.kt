package ru.terrakok.gitlabclient.ui.global

import android.support.design.widget.Snackbar
import com.arellomobile.mvp.MvpAppCompatFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseFragment : MvpAppCompatFragment() {
    companion object {
        private val PROGRESS_TAG = "bf_progress"
    }

    protected fun showProgressDialog(progress: Boolean) {
        val fragment = fragmentManager?.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !progress) {
            (fragment as ProgressDialog).dismiss()
        } else if (fragment == null && progress) {
            ProgressDialog().show(fragmentManager, PROGRESS_TAG)
        }
    }

    protected fun showSnackMessage(message: String) {
        Snackbar.make(view!!.rootView, message, Snackbar.LENGTH_LONG).show()
    }

    open fun onBackPressed() {}
}
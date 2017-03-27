package ru.terrakok.gitlabclient.view.global

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_base.view.*
import ru.terrakok.gitlabclient.R

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseFragment : MvpAppCompatFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_base, container, false)
        rootView.baseContentContainer.addView(inflater.inflate(getLayoutId(), rootView.baseContentContainer, false))
        return rootView
    }

    abstract fun getLayoutId(): Int

    protected fun showProgressView(isVisible: Boolean) {
        progressView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    protected fun isProgress() = progressView.visibility == View.VISIBLE

    protected fun showSnackMessage(message: String) {
        Snackbar.make(baseContentContainer, message, Snackbar.LENGTH_LONG).show()
    }

    open fun onBackPressed() {}
}
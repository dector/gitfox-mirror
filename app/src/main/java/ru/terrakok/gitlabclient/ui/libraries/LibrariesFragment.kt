package ru.terrakok.gitlabclient.ui.libraries

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_libraries.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.extension.tryOpenLink
import ru.terrakok.gitlabclient.presentation.libraries.LibrariesPresenter
import ru.terrakok.gitlabclient.presentation.libraries.LibrariesView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.AppLibraryAdapterDelegate
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.12.17.
 */
class LibrariesFragment : BaseFragment(), LibrariesView {
    override val layoutRes = R.layout.fragment_libraries

    private val adapter: LibraryAdapter by lazy { LibraryAdapter() }

    @InjectPresenter lateinit var presenter: LibrariesPresenter

    @ProvidePresenter
    fun providePresenter(): LibrariesPresenter =
            Toothpick
                    .openScope(DI.SERVER_SCOPE)
                    .getInstance(LibrariesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@LibrariesFragment.adapter
        }
    }

    override fun showLibraries(libraries: List<AppLibrary>) {
        adapter.setData(libraries)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    private inner class LibraryAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(AppLibraryAdapterDelegate({ tryOpenLink(it.url) }))
        }

        fun setData(libraries: List<AppLibrary>) {
            items.clear()
            items.addAll(libraries)

            notifyDataSetChanged()
        }
    }
}
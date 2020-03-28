package ru.terrakok.gitlabclient.ui.libraries

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import gitfox.entity.app.develop.AppLibrary
import kotlinx.android.synthetic.main.fragment_libraries.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.presentation.libraries.LibrariesPresenter
import ru.terrakok.gitlabclient.presentation.libraries.LibrariesView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.AppLibraryAdapterDelegate
import ru.terrakok.gitlabclient.util.addSystemBottomPadding
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.tryOpenLink

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.12.17.
 */
class LibrariesFragment : BaseFragment(), LibrariesView {
    override val layoutRes = R.layout.fragment_libraries

    override val parentScopeName = DI.APP_SCOPE

    private val adapter: LibraryAdapter by lazy { LibraryAdapter() }

    @InjectPresenter
    lateinit var presenter: LibrariesPresenter

    @ProvidePresenter
    fun providePresenter(): LibrariesPresenter =
        scope.getInstance(LibrariesPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
        toolbar.addSystemTopPadding()

        with(recyclerView) {
            addSystemBottomPadding()
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

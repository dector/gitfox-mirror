package ru.terrakok.gitlabclient.ui.global

import android.content.Context
import com.arellomobile.mvp.MvpAppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
open class BaseActivity : MvpAppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}
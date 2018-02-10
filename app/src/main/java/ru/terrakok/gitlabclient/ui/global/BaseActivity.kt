package ru.terrakok.gitlabclient.ui.global

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.ui.auth.AuthActivity
import ru.terrakok.gitlabclient.ui.issue.IssueActivity
import ru.terrakok.gitlabclient.ui.launch.MainActivity
import ru.terrakok.gitlabclient.ui.mergerequest.MergeRequestActivity
import ru.terrakok.gitlabclient.ui.project.ProjectActivity
import ru.terrakok.gitlabclient.ui.user.UserActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
abstract class BaseActivity : MvpAppCompatActivity() {

    abstract val layoutRes: Int

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }
    
    protected fun getFlowIntent(flowKey: String, data: Any?): Intent? = when (flowKey) {
        Screens.AUTH_FLOW -> AuthActivity.getStartIntent(this)
        Screens.MAIN_FLOW -> MainActivity.getStartIntent(this)
        Screens.PROJECT_FLOW -> {
            val (projectId, projectName) = data as Pair<Long, String>
            ProjectActivity.getStartIntent(projectId, projectName, this)
        }
        Screens.USER_FLOW -> UserActivity.getStartIntent(data as Long, this)
        Screens.MR_FLOW -> {
            val (projectId, mrId) = data as Pair<Long, Long>
            MergeRequestActivity.getStartIntent(projectId, mrId, this)
        }
        Screens.ISSUE_FLOW -> {
            val (projectId, issueId) = data as Pair<Long, Long>
            IssueActivity.getStartIntent(projectId, issueId, this)
        }
        else -> null
    }
}
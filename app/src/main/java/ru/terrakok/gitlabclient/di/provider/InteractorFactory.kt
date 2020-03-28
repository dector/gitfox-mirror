package ru.terrakok.gitlabclient.di.provider

import gitfox.SDK
import gitfox.model.interactor.*
import javax.inject.Inject
import javax.inject.Provider

class AccountInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<AccountInteractor> {
    override fun get() = sdk.getAccountInteractor()
}

class AppInfoInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<AppInfoInteractor> {
    override fun get() = sdk.getAppInfoInteractor()
}

class CommitInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<CommitInteractor> {
    override fun get() = sdk.getCommitInteractor()
}

class EventInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<EventInteractor> {
    override fun get() = sdk.getEventInteractor()
}

class IssueInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<IssueInteractor> {
    override fun get() = sdk.getIssueInteractor()
}

class LabelInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<LabelInteractor> {
    override fun get() = sdk.getLabelInteractor()
}

class LaunchInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<LaunchInteractor> {
    override fun get() = sdk.getLaunchInteractor()
}

class MembersInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<MembersInteractor> {
    override fun get() = sdk.getMembersInteractor()
}

class MergeRequestInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<MergeRequestInteractor> {
    override fun get() = sdk.getMergeRequestInteractor()
}

class MilestoneInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<MilestoneInteractor> {
    override fun get() = sdk.getMilestoneInteractor()
}

class ProjectInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<ProjectInteractor> {
    override fun get() = sdk.getProjectInteractor()
}

class SessionInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<SessionInteractor> {
    override fun get() = sdk.getSessionInteractor()
}

class TodoInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<TodoInteractor> {
    override fun get() = sdk.getTodoInteractor()
}

class UserInteractorProvider @Inject constructor(
    private val sdk: SDK
) : Provider<UserInteractor> {
    override fun get() = sdk.getUserInteractor()
}

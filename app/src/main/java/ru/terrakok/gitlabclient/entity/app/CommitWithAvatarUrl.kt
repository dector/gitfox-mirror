package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.Commit

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
data class CommitWithAvatarUrl(val commit: Commit, val authorAvatarUrl: String?)
package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.Commit
import ru.terrakok.gitlabclient.entity.ShortUser

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
data class CommitWithShortUser(val commit: Commit, val shortUser: ShortUser?)

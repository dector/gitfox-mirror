package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.Author
import ru.terrakok.gitlabclient.entity.Commit

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
data class CommitWithAuthor(val commit: Commit, val author: Author?)
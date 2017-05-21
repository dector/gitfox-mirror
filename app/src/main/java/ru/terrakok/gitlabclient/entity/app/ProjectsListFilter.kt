package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.common.OrderBy
import ru.terrakok.gitlabclient.entity.common.Sort
import ru.terrakok.gitlabclient.entity.common.Visibility

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
data class ProjectsListFilter(
        var archived: Boolean? = null,
        var visibility: Visibility? = null,
        var order_by: OrderBy? = null,
        var sort: Sort? = null,
        var search: String? = null,
        var simple: Boolean? = null,
        var owned: Boolean? = null,
        var membership: Boolean? = null,
        var starred: Boolean? = null
)
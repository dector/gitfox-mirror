package ru.terrakok.gitlabclient.entity.target.issue

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import ru.terrakok.gitlabclient.entity.Links
import ru.terrakok.gitlabclient.entity.target.Target

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 13.09.17
 */
class Issue : Target() {
    @SerializedName("due_date")
    val dueDate: LocalDate? = null
    @SerializedName("confidential")
    private val _confidential: Boolean? = null
    @SerializedName("weight")
    val weight: Long? = null
    @SerializedName("_links")
    val links: Links? = null

    val confidential get() = _confidential!!
}

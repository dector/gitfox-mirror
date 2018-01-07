package ru.terrakok.gitlabclient.entity.target

import com.google.gson.annotations.SerializedName
import ru.terrakok.gitlabclient.entity.Assignee
import ru.terrakok.gitlabclient.entity.Author
import ru.terrakok.gitlabclient.entity.Milestone
import java.util.*

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 13.09.17
 */
abstract class Target {
    @SerializedName("id") private val _id: Long? = null
    @SerializedName("iid") private val _iid: Long? = null
    @SerializedName("project_id") private val _projectId: Long? = null
    @SerializedName("title") private val _title: String? = null
    @SerializedName("state") private val _state: TargetState? = null
    @SerializedName("updated_at") val updatedAt: Date? = null
    @SerializedName("created_at") val createdAt: Date? = null
    @SerializedName("labels") private val _labels: List<String>? = null
    @SerializedName("milestone") val milestone: Milestone? = null
    @SerializedName("assignees") private val _assignees: List<Assignee>? = null
    @SerializedName("author") val _author: Author? = null
    @SerializedName("assignee") val assignee: Assignee? = null
    @SerializedName("user_notes_count") private val _userNotesCount: Int? = null
    @SerializedName("upvotes") private val _upVotes: Int? = null
    @SerializedName("downvotes") private val _downVotes: Int? = null
    @SerializedName("web_url") val webUrl: String? = null
    @SerializedName("subscribed") private val _subscribed: Boolean? = null
    @SerializedName("time_stats") val timeStats: TimeStats? = null

    val id get() = _id!!
    val iid get() = _iid!!
    val projectId get() = _projectId!!
    val title get() = _title!!
    val state get() = _state!!
    val labels get() = _labels!!
    val assignees get() = _assignees!!
    val userNotesCount get() = _userNotesCount!!
    val upVotes get() = _upVotes!!
    val downVotes get() = _downVotes!!
    val subscribed get() = _subscribed!!
    val author get() = _author!!

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Target) return false

        if (id != other.id) return false
        if (iid != other.iid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + iid.hashCode()
        return result
    }
}
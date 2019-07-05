package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class User(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String,
    @SerializedName("state") val state: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("web_url") val webUrl: String?,
    @SerializedName("created_at") val createdAt: ZonedDateTime,
    @SerializedName("is_admin") val isAdmin: Boolean,
    @SerializedName("bio") val bio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("skype") val skype: String?,
    @SerializedName("linkedin") val linkedin: String?,
    @SerializedName("twitter") val twitter: String?,
    @SerializedName("website_url") val websiteUrl: String?,
    @SerializedName("organization") val organization: String?,
    @SerializedName("last_sign_in_at") val lastSignInAt: ZonedDateTime,
    @SerializedName("confirmed_at") val confirmedAt: ZonedDateTime,
    @SerializedName("color_scheme_id") val colorSchemeId: Long,
    @SerializedName("projects_limit") val projectsLimit: Long,
    @SerializedName("current_sign_in_at") val currentSignInAt: ZonedDateTime,
    @SerializedName("identities") val identities: List<Identity>?,
    @SerializedName("can_create_group") val canCreateGroup: Boolean,
    @SerializedName("can_create_project") val canCreateProject: Boolean,
    @SerializedName("two_factor_enabled") val twoFactorEnabled: Boolean,
    @SerializedName("external") val external: Boolean
)

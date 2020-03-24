@file:UseSerializers(TimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ru.terrakok.gitlabclient.model.data.server.deserializer.TimeDeserializer

@Serializable
data class User(
    @SerialName("id") val id: Long,
    @SerialName("username") val username: String,
    @SerialName("email") val email: String? = null,
    @SerialName("name") val name: String,
    @SerialName("state") val state: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("created_at") val createdAt: Time,
    @SerialName("is_admin") val isAdmin: Boolean = false,
    @SerialName("bio") val bio: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("skype") val skype: String? = null,
    @SerialName("linkedin") val linkedin: String? = null,
    @SerialName("twitter") val twitter: String? = null,
    @SerialName("website_url") val websiteUrl: String? = null,
    @SerialName("organization") val organization: String? = null,
    @SerialName("last_sign_in_at") val lastSignInAt: Time? = null,
    @SerialName("confirmed_at") val confirmedAt: Time? = null,
    @SerialName("color_scheme_id") val colorSchemeId: Long? = null,
    @SerialName("projects_limit") val projectsLimit: Long? = null,
    @SerialName("current_sign_in_at") val currentSignInAt: Time? = null,
    @SerialName("identities") val identities: List<Identity>? = null,
    @SerialName("can_create_group") val canCreateGroup: Boolean? = null,
    @SerialName("can_create_project") val canCreateProject: Boolean? = null,
    @SerialName("two_factor_enabled") val twoFactorEnabled: Boolean? = null,
    @SerialName("external") val external: Boolean? = null
)

@Serializable
data class ShortUser(
    @SerialName("id") val id: Long,
    @SerialName("state") val state: String? = null,
    @SerialName("name") val name: String,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("username") val username: String
)

@Serializable
data class Identity(
    @SerialName("provider") val provider: String,
    @SerialName("extern_uid") val externUid: String
)

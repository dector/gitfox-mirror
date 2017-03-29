package ru.terrakok.gitlabclient.model.server

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import ru.terrakok.gitlabclient.entity.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
interface GitlabApi {

    @FormUrlEncoded
    @POST("oauth/token")
    fun auth(
            @Field("client_id") appId: String,
            @Field("client_secret") appKey: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirectUri: String,
            @Field("grant_type") type: String = "authorization_code"
    ): Single<TokenData>

    @GET("api/v4/projects")
    fun getProjects(
            @Field("archived") archived: Boolean? = null,
            @Field("visibility") visibility: Visibility? = null,
            @Field("order_by") order_by: OrderBy? = null,
            @Field("sort") sort: Sort? = null,
            @Field("search") search: String? = null,
            @Field("simple") simple: Boolean? = null,
            @Field("owned") owned: Boolean? = null,
            @Field("membership") membership: Boolean? = null,
            @Field("starred") starred: Boolean? = null
    ): Single<List<Project>>
}
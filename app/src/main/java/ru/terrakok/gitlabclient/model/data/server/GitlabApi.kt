package ru.terrakok.gitlabclient.model.data.server

import io.reactivex.Single
import retrofit2.http.*
import ru.terrakok.gitlabclient.entity.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
interface GitlabApi {
    companion object {
        const val API_PATH = "api/v4"
    }

    @FormUrlEncoded
    @POST("oauth/token")
    fun auth(
            @Field("client_id") appId: String,
            @Field("client_secret") appKey: String,
            @Field("code") code: String,
            @Field("redirect_uri") redirectUri: String,
            @Field("grant_type") type: String = "authorization_code"
    ): Single<TokenData>

    @GET("$API_PATH/projects")
    fun getProjects(
            @Query("archived") archived: Boolean?,
            @Query("visibility") visibility: Visibility?,
            @Query("order_by") order_by: OrderBy?,
            @Query("sort") sort: Sort?,
            @Query("search") search: String?,
            @Query("simple") simple: Boolean?,
            @Query("owned") owned: Boolean?,
            @Query("membership") membership: Boolean?,
            @Query("starred") starred: Boolean?,
            @Query("page") page: Int,
            @Query("per_page") pageSize: Int
    ): Single<List<Project>>

    @GET("$API_PATH/user")
    fun getMyUser(): Single<User>
}
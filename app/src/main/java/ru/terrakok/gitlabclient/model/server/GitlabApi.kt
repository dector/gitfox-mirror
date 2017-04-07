package ru.terrakok.gitlabclient.model.server

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
            @Query("archived") archived: Boolean? = null,
            @Query("visibility") visibility: Visibility? = null,
            @Query("order_by") order_by: OrderBy? = null,
            @Query("sort") sort: Sort? = null,
            @Query("search") search: String? = null,
            @Query("simple") simple: Boolean? = null,
            @Query("owned") owned: Boolean? = null,
            @Query("membership") membership: Boolean? = null,
            @Query("starred") starred: Boolean? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") pageSize: Int = 20
    ): Single<List<Project>>

    @GET("$API_PATH/user")
    fun getMyUser(): Single<User>
}
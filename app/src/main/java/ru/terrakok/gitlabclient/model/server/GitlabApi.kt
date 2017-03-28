package ru.terrakok.gitlabclient.model.server

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.terrakok.gitlabclient.entity.TokenData

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
}
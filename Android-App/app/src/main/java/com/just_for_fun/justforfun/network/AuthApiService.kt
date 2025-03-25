package com.just_for_fun.justforfun.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email: String, @Field("password") password: String): Response<Void>

    @FormUrlEncoded
    @POST("signup")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<Void>
}

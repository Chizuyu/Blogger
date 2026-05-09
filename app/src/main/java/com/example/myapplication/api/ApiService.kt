package com.example.myapplication.api

import com.example.myapplication.model.LikeRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.Post
import com.example.myapplication.model.Profile
import com.example.myapplication.model.Register
import com.example.myapplication.model.RegisterResponse
import com.example.myapplication.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService{

    //GET
    @GET("api/users")
    suspend fun getUsers(): List<User>

    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @GET("api/me/post")
    suspend fun getMyPost(@Header("Authorization") token: String): List<Post>

    @GET("api/me/post/liked")
    suspend fun getLikedPost(@Header("Authorization") token: String): List<Post>

    @GET("api/posts/{id}")
    suspend fun getPostDetail(@Path("id") id: String): Post

    @GET("api/posts/{postId}/total-count")
    suspend fun getPostDetailLikes(@Path("postId") postId: String): Int

    @GET("api/me")
    suspend fun getProfile(@Header("Authorization") token: String): Profile

    @GET("api/me/is-liked-post/{postId}")
    suspend fun checkIsLiked(
        @Path("postId") postId: String,
        @Header("Authorization") token: String
    ): Boolean

    //POST

    @POST("api/auth/register")
    suspend fun register(@Body body: Register): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body body: User): LoginResponse

    @POST("api/posts/like")
    suspend fun toggleLike(
        @Body body: LikeRequest,
        @Header("Authorization") token: String
    ): Unit

    //PUT
    @PUT("api/posts/{id}")
    suspend fun putLikedPost(
        @Path("id") id: String, @Header("Authorization") token: String): Post
}
package com.example.myapplication.api

import com.example.myapplication.model.Category
import com.example.myapplication.model.Comment
import com.example.myapplication.model.CommentRequest
import com.example.myapplication.model.CreatePostRequest
import com.example.myapplication.model.IsLikedResponse
import com.example.myapplication.model.LikeCountResponse
import com.example.myapplication.model.LikeRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.Post
import com.example.myapplication.model.Profile
import com.example.myapplication.model.Register
import com.example.myapplication.model.RegisterResponse
import com.example.myapplication.model.UpdateProfileRequest
import com.example.myapplication.model.User
import okhttp3.MultipartBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{

    //GET
    @GET("api/users")
    suspend fun getUsers(): List<User>

    @GET("api/posts")
    suspend fun getPosts(): List<Post>

    @GET("api/me/post")
    suspend fun getMyPost(@Header("Authorization") token: String): List<Post>

    @GET("api/posts/{id}")
    suspend fun getPostDetail(@Path("id") id: String): Post

    @GET("api/me")
    suspend fun getProfile(@Header("Authorization") token: String): Profile

    //Liked/Unliked Post

    @GET("api/me/post/liked")
    suspend fun getLikedPost(@Header("Authorization") token: String): List<Post>

    @GET("api/posts/{postId}/total-count")
    suspend fun getPostDetailLikes(@Path("postId") postId: String): LikeCountResponse

    @GET("api/me/is-liked-post/{postId}")
    suspend fun checkIsLiked(
        @Path("postId") postId: String,
        @Header("Authorization") token: String
    ): IsLikedResponse

    @GET("api/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): User

    @PUT("api/me")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @Multipart
    @POST("api/me/photo")
    suspend fun uploadProfilePhoto(
        @Part photo: MultipartBody.Part,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    //POST

    @POST("api/auth/register")
    suspend fun register(@Body body: Register): RegisterResponse

    @POST("api/auth/login")
    suspend fun login(@Body body: User): LoginResponse

    @POST("api/posts/like")
    suspend fun toggleLike(
        @Body request: LikeRequest,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @POST("api/posts")
    suspend fun createPost(
        @Body request: CreatePostRequest,
        @Header("Authorization") token: String
    ): retrofit2.Response<Post>

    @Multipart
    @POST("api/posts/{postId}/thumbnail")
    suspend fun uploadThumbnail(
        @Path("postId") postId: String,
        @Part photo: MultipartBody.Part,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @GET("api/Categories")
    suspend fun getCategories(): List<Category>

    @Multipart
    @POST("api/posts/{postId}/image")
    suspend fun uploadImageContent(
        @Path("postId") postId: String,
        @Part photo: MultipartBody.Part,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    //PUT

    @PUT("api/me")
    suspend fun updateProfileMap(
        @Body data: Map<String, @JvmSuppressWildcards Any>,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @PUT("api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: String,
        @Body request: CreatePostRequest, // Re-use model yang sama
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @DELETE("api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @GET("api/posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: String): List<Comment>

    @POST("api/posts/{postId}/comments")
    suspend fun postComment(
        @Path("postId") postId: String,
        @Body request: CommentRequest,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @DELETE("api/posts/{postId}/comments/{id}")
    suspend fun deleteComment(
        @Path("postId") postId: String,
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): retrofit2.Response<Unit>

    @GET("api/Users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @GET("api/Posts")
    suspend fun getPosts(
        @Query("title") title: String? = null,
        @Query("categoryId") categoryId: String? = null
    ): List<Post>
}
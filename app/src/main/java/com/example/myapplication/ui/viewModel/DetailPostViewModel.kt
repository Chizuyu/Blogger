package com.example.myapplication.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Comment
import com.example.myapplication.model.CommentRequest
import com.example.myapplication.model.LikeRequest
import com.example.myapplication.model.Post
import com.example.myapplication.util.GlobalData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailPostViewModel : ViewModel() {
    var post by mutableStateOf<Post?>(null)
    var likeCount by mutableIntStateOf(0)
    var isOwnPost by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var isLikeLoading by mutableStateOf(false)
    var isLiked by mutableStateOf(false)

    var commentList by mutableStateOf<List<Comment>>(emptyList())
    var commentText by mutableStateOf("")
    var isCommentLoading by mutableStateOf(false)

    fun fetchPostDetail(postId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val token = "Bearer ${GlobalData.tokenUser}"

                val detailReq = async { RetroFitClient.instance.getPostDetail(postId) }
                val likesReq = async { RetroFitClient.instance.getPostDetailLikes(postId) }
                val isLikedReq = async { RetroFitClient.instance.checkIsLiked(postId, token) }

                val postResult = detailReq.await()

                post = postResult
                isOwnPost = postResult.user.id == GlobalData.myUserId

                post = detailReq.await()
                post = detailReq.await()
                likeCount = likesReq.await().count
                isLiked = isLikedReq.await().isLiked
            } catch (e: Exception) {
                Log.e("DETAIL_ERROR", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }
    fun likePost(postId: String) {
        viewModelScope.launch {
            isLikeLoading = true
            try {
                val token = "Bearer ${GlobalData.tokenUser}"

                val response = RetroFitClient.instance.toggleLike(LikeRequest(postId), token)

                if (response.isSuccessful) {
                    val countTask = async { RetroFitClient.instance.getPostDetailLikes(postId) }
                    val statusTask = async { RetroFitClient.instance.checkIsLiked(postId, token) }

                    likeCount = countTask.await().count
                    isLiked = statusTask.await().isLiked
                } else {
                    Log.e("LIKE_ERROR", "Server returned: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("LIKE_ERROR", e.message.toString())
            } finally {
                isLikeLoading = false
            }
        }
    }

    fun fetchComments(postId: String) {
        viewModelScope.launch {
            try {
                val response = RetroFitClient.instance.getComments(postId)
                commentList = response
            } catch (e: Exception) {
                Log.e("FETCH_COMMENTS", e.message.toString())
            }
        }
    }

    fun sendComment(postId: String) {
        if (commentText.isBlank()) return
        viewModelScope.launch {
            isCommentLoading = true
            try {
                val token = "Bearer ${GlobalData.tokenUser}"
                val response = RetroFitClient.instance.postComment(postId, CommentRequest(commentText), token)
                if (response.isSuccessful) {
                    commentText = ""
                    fetchComments(postId)
                }
            } catch (e: Exception) {
                Log.e("SEND_COMMENT", e.message.toString())
            } finally {
                isCommentLoading = false
            }
        }
    }

    fun deleteComment(postId: String, commentId: String) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${GlobalData.tokenUser}"
                val response = RetroFitClient.instance.deleteComment(postId, commentId, token)

                if (response.isSuccessful) {
                    fetchComments(postId)
                }
            } catch (e: Exception) {
                Log.e("DELETE_COMMENT_ERROR", e.message.toString())
            }
        }
    }
}
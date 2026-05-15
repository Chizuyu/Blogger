package com.example.myapplication.ui.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.LikeRequest
import com.example.myapplication.model.Post
import com.example.myapplication.util.GlobalData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailPostViewModel : ViewModel() {
    var post by mutableStateOf<Post?>(null)
    var likeCount by mutableIntStateOf(0)
    var isLoading by mutableStateOf(false)
    var isLikeLoading by mutableStateOf(false)
    var isLiked by mutableStateOf(false)

    fun fetchPostDetail(postId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val token = "Bearer ${GlobalData.tokenUser}"

                val detailReq = async { RetroFitClient.instance.getPostDetail(postId) }
                val likesReq = async { RetroFitClient.instance.getPostDetailLikes(postId) }
                val isLikedReq = async { RetroFitClient.instance.checkIsLiked(postId, token) }

                post = detailReq.await()
                likeCount = likesReq.await()
                isLiked = isLikedReq.await()
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

                // 1. Jalankan aksi Like/Unlike ke server
                RetroFitClient.instance.toggleLike(LikeRequest(postId), token)

                // 2. Ambil data terbaru dari server
                // Jalankan paralel agar responsif
                val updatedLikesReq = async { RetroFitClient.instance.getPostDetailLikes(postId) }
                val updatedStatusReq = async { RetroFitClient.instance.checkIsLiked(postId, token) }

                // 3. Update State (PENTING: Masukkan hasil await ke variabel state)
                likeCount = updatedLikesReq.await()
                isLiked = updatedStatusReq.await()

            } catch (e: Exception) {
                Log.e("LIKE_ERROR", e.message.toString())
            } finally {
                isLikeLoading = false
            }
        }
    }
}
package com.example.myapplication.ui.viewModel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.FollowModel
import com.example.myapplication.util.GlobalData
import kotlinx.coroutines.launch

class FollowViewModel : ViewModel() {
    var followList by mutableStateOf<List<FollowModel>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun fetchList(userId: String, isFollowers: Boolean) {
        viewModelScope.launch {
            isLoading = true
            try {
                val token = "Bearer ${GlobalData.tokenUser}"
                val response = if (isFollowers) {
                    RetroFitClient.instance.getFollowers(token, userId)
                } else {
                    RetroFitClient.instance.getFollowing(token, userId)
                }

                if (response.isSuccessful) {
                    followList = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                isLoading = false
            }
        }
    }
}
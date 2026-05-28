package com.example.myapplication.ui.viewModel

import android.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Post
import com.example.myapplication.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    var userSearchQuery by mutableStateOf("")
    var userList by mutableStateOf<List<User>>(emptyList())


    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var selectedUser by mutableStateOf<User?>(null)
    var userPosts by mutableStateOf<List<Post>>(emptyList())
    var searchQuery by mutableStateOf("")
    var isFollowing by mutableStateOf(false)


    fun getUsers(){
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = RetroFitClient.instance.getUsers()

                userList = response
            }catch (e: Exception) {
                errorMessage = "Error Connection ${e.message}"
            }finally {
                isLoading = false
            }
        }
    }
    init {
        getUsers()
    }

    fun fetchUserDetail(userId: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                selectedUser = RetroFitClient.instance.getUserById(userId)
                val allPosts = RetroFitClient.instance.getPosts()
                userPosts = allPosts.filter { it.user.id == userId }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                isLoading = false
            }
        }
    }

    fun onSearch(query: String) {
        userSearchQuery = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            isLoading = true
            try {
                userList = RetroFitClient.instance.getUsers(name = query)
            } catch (e: Exception) { /* handle error */ }
            finally { isLoading = false }
        }
    }
    private var searchJob: Job? = null

    fun toggleFollow(targetUserId: String) {
        viewModelScope.launch {
            try {
                val response = RetroFitClient.instance.toggleFollow(targetUserId)
                if (response.isSuccessful) {
                    isFollowing = response.body()?.isFollowing ?: false
                } else {
                    errorMessage = "Gagal memproses follow"
                }
            } catch (e: Exception) {
                errorMessage = "Koneksi bermasalah: ${e.message}"
            }
        }
    }
}
package com.example.myapplication.ui.viewModel

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
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    var userList by mutableStateOf<List<User>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var selectedUser by mutableStateOf<User?>(null)
    var selectedUserPosts by mutableStateOf<List<Post>>(emptyList())

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
                // 1. Ambil data profil user
                selectedUser = RetroFitClient.instance.getUserById(userId)

                // 2. Ambil semua post dan filter yang milik user ini
                val allPosts = RetroFitClient.instance.getPosts()
                selectedUserPosts = allPosts.filter { it.user.id == userId }

            } catch (e: Exception) {
                errorMessage = e.message.toString()
            } finally {
                isLoading = false
            }
        }
    }
}
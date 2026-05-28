package com.example.myapplication.ui.viewModel

import android.R
import android.util.Log
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
import com.example.myapplication.util.GlobalData
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
                val token = "Bearer ${GlobalData.tokenUser}"

                val userFromServer = RetroFitClient.instance.getUserById(token, userId)

                selectedUser = userFromServer
                isFollowing = userFromServer.isFollowing

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
                val token = "Bearer ${GlobalData.tokenUser}"

                val response = RetroFitClient.instance.toggleFollow(token, targetUserId)
                if (response.isSuccessful) {
                    isFollowing = response.body()?.isFollowing ?: false

                    fetchUserDetail(targetUserId)

                    Log.d("FOLLOW_DEBUG", "Toggle sukses, merefresh data user...")
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}
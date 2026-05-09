package com.example.myapplication.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Post
import com.example.myapplication.model.Profile
import com.example.myapplication.util.GlobalData
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var profileData by mutableStateOf<Profile?>(null)
    var postList by mutableStateOf<List<Post>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    val token = "Bearer ${GlobalData.tokenUser}"


    fun fetchProfile(){
        viewModelScope.launch {
            isLoading = true

            try {
                val response = RetroFitClient.instance.getProfile(token)
                profileData = response
            }catch (e: Exception){
                errorMessage = "API_ERROR ${e.message}"
            }finally {
                isLoading = false
            }
        }
    }
    fun fetchLikedPost() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = RetroFitClient.instance.getLikedPost(token)
                postList = response
            }catch (e: Exception){
                errorMessage = "API_ERROR"
            }finally {
                isLoading = false
            }
        }
    }
    fun fetchMyPost() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = RetroFitClient.instance.getMyPost(token)

                postList = response
            }catch (e: Exception){
                errorMessage = "API_ERROR"
            }finally {
                isLoading = false
            }
        }
    }
}
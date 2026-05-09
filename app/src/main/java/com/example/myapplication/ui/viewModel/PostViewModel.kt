package com.example.myapplication.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Post
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    var postList by mutableStateOf<List<Post>>(emptyList())

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun getPosts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val response = RetroFitClient.instance.getPosts()

                postList = response
            }catch (e: Exception){
                errorMessage = "API_ERROR"
            }finally {
                isLoading = false
            }
        }
    }
    init {
        getPosts()
    }
}
package com.example.myapplication.ui.viewModel

import android.provider.Settings
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.User
import com.example.myapplication.util.GlobalData
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun doLogin(user: String, pass: String, onSuccess: () -> Unit) {
        if (user.isEmpty() || pass.isEmpty()) {
            errorMessage = "All Fields Cannot Be Empty"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                val response = RetroFitClient.instance.login(User(username = user, password = pass))

                GlobalData.tokenUser = response.token
                GlobalData.myUserId = response.user.id ?: ""
                onSuccess()

            } catch (e: retrofit2.HttpException) {
                if (e.code() == 401) {
                    errorMessage = "Invalid Username or Password"
                } else {
                    errorMessage = "Server Error: ${e.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Connection Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
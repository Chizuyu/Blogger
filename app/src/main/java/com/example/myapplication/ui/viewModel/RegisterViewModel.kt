package com.example.myapplication.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Register
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class RegisterViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun doRegister(userData: Register, onSuccess:() -> Unit){
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                val users = RetroFitClient.instance.getUsers()
                val isMatch = users.any{
                    it.username == userData.username
                }

                if(isMatch){
                    errorMessage = "Username has Already been taken"
                }else{
                    val request = Register(
                        id = "",
                        firstName = userData.firstName,
                        lastName = userData.lastName,
                        username = userData.username,
                        password = userData.password,
                        dateOfBirth = userData.dateOfBirth,
                        joinDate = LocalDateTime.now().toString(),
                        photo = ""
                    )

                    val response = RetroFitClient.instance.register(request)
                    onSuccess()
                }
            }catch (e: Exception) {
                errorMessage = "Connection Error ${e.message}"
            }finally {
                isLoading = false
            }
        }
    }
}
package com.example.myapplication.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Post
import com.example.myapplication.model.Profile
import com.example.myapplication.model.UpdateProfileRequest
import com.example.myapplication.model.User
import com.example.myapplication.util.GlobalData
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileViewModel : ViewModel() {
    var profileData by mutableStateOf<Profile?>(null)
    var postList by mutableStateOf<List<Post>>(emptyList())

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var selectedFileName by mutableStateOf("File Name")
    var selectedImageUri by mutableStateOf<Uri?>(null)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    private val token: String
        get() = "Bearer ${GlobalData.tokenUser}"

    fun fetchProfile() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetroFitClient.instance.getCurrentUser(token)
                profileData = Profile(
                    firstName = response.firstName,
                    lastName = response.lastName,
                    username = response.username,
                    joinDate = response.joinDate,
                    dateOfBirth = response.dateOfBirth,
                    photo = response.photo
                )
                firstName = response.firstName ?: ""
                lastName = response.lastName ?: ""
                username = response.username ?: ""
            } catch (e: Exception) {
                errorMessage = "Gagal mengambil profil"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfileData(context: Context, onSuccess: () -> Unit) {
        if (password.isEmpty()) {
            errorMessage = "Password wajib diisi untuk verifikasi update"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Password dan konfirmasi tidak cocok"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                val request = UpdateProfileRequest(
                    firstName = firstName,
                    lastName = lastName,
                    username = username,
                    password = password
                )

                val response = RetroFitClient.instance.updateProfile(request, token)

                if (response.isSuccessful) {
                    selectedImageUri?.let { uploadPhoto(context, it, token) }
                    onSuccess()
                } else {
                    errorMessage = "Gagal Update: Kode ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Update Gagal: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun uploadPhoto(context: Context, uri: Uri, token: String) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes() ?: return
            val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("photo", "profile.jpg", requestFile)
            RetroFitClient.instance.uploadProfilePhoto(body, token)
        } catch (e: Exception) {
            Log.e("UPLOAD_ERROR", e.toString())
        }
    }

    fun fetchLikedPost() {
        viewModelScope.launch {
            isLoading = true
            try {
                postList = RetroFitClient.instance.getLikedPost(token)
            } catch (e: Exception) {
                errorMessage = "API_ERROR_LIKED"
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchMyPost() {
        viewModelScope.launch {
            isLoading = true
            try {
                postList = RetroFitClient.instance.getMyPost(token)
            } catch (e: Exception) {
                errorMessage = "API_ERROR_MYPOST"
            } finally {
                isLoading = false
            }
        }
    }
}
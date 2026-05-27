package com.example.myapplication.ui.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.api.RetroFitClient
import com.example.myapplication.model.Category
import com.example.myapplication.model.CreatePostRequest
import com.example.myapplication.model.Post
import com.example.myapplication.util.GlobalData
import com.example.myapplication.util.toMultipartBody // Pastikan import helper ini
import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class PostViewModel : ViewModel() {

    // -- UI State --
    var postList by mutableStateOf<List<Post>>(emptyList())
    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var selectedCategory by mutableStateOf<Category?>(null)

    // File Selection State
    var thumbnailUri by mutableStateOf<Uri?>(null)
    var thumbnailName by mutableStateOf("File Name")
    var imageContentUri by mutableStateOf<Uri?>(null)
    var imageContentName by mutableStateOf("File Name")

    // API Status State
    var categories by mutableStateOf<List<Category>>(emptyList())
    var isLoading by mutableStateOf(false)
    var isCreateSuccess by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    var searchQuery by mutableStateOf("")


    init {
        getPosts()
        fetchCategories() // Panggil ini agar dropdown terisi saat app buka
    }

    fun getPosts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                val response = RetroFitClient.instance.getPosts()
                postList = response
            } catch (e: Exception) {
                errorMessage = "Gagal mengambil data post"
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetroFitClient.instance.getCategories()
                categories = response
            } catch (e: Exception) {
                // Jika API Categories belum ada, bisa gunakan dummy untuk sementara:
                /*
                categories = listOf(
                    Category("1", "Technology"),
                    Category("2", "LifeStyle")
                )
                */
            }
        }
    }

    fun createFullPost(context: Context) {
        val catId = selectedCategory?.id
        if (catId == null) {
            errorMessage = "Pilih kategori terlebih dahulu"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                val token = "Bearer ${GlobalData.tokenUser}"

                // 1. Tetap buat ID dummy untuk request, tapi jangan diandalkan
                val dummyId = UUID.randomUUID().toString()
                val request = CreatePostRequest(
                    id = dummyId,
                    categoryId = catId,
                    title = title,
                    content = content
                )

                Log.d("DEBUG_UPLOAD", "1. Mencoba Create Post...")
                val response = RetroFitClient.instance.createPost(request, token)

                if (response.isSuccessful && response.body() != null) {
                    // --- KUNCI PERBAIKAN DI SINI ---
                    // Ambil ID asli yang diberikan oleh server dari response body
                    val actualPostId = response.body()!!.id
                    Log.d("DEBUG_UPLOAD", "2. Post Berhasil! ID Server: $actualPostId")

                    // Gunakan actualPostId (ID dari server), BUKAN dummyId
                    thumbnailUri?.let { uri ->
                        val res = uploadFile(context, uri, actualPostId, "thumbnail", token)
                        Log.d("DEBUG_UPLOAD", "Hasil Thumbnail: $res")
                    }

                    imageContentUri?.let { uri ->
                        val res = uploadFile(context, uri, actualPostId, "image", token)
                        Log.d("DEBUG_UPLOAD", "Hasil Image: $res")
                    }

                    isCreateSuccess = true
                } else {
                    errorMessage = "Gagal Create Post: ${response.code()}"
                    Log.e("DEBUG_UPLOAD", "Gagal: $errorMessage")
                }
            } catch (e: Exception) {
                errorMessage = "Exception: ${e.localizedMessage}"
                Log.e("DEBUG_UPLOAD", "Error: ", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Fungsi Update (Hampir sama dengan create, tapi panggil PUT)
    fun updateFullPost(context: Context, postId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                val token = "Bearer ${GlobalData.tokenUser}"
                val request = CreatePostRequest(
                    id = postId,
                    categoryId = selectedCategory?.id!!,
                    title = title,
                    content =  content
                )

                val response = RetroFitClient.instance.updatePost(postId, request, token)
                if (response.isSuccessful) {
                    // Upload gambar baru JIKA user memilih file baru (Uri tidak null)
                    thumbnailUri?.let { uploadFile(context, it, postId, "thumbnail", token) }
                    imageContentUri?.let { uploadFile(context, it, postId, "image", token) }

                    isCreateSuccess = true
                }
            } catch (e: Exception) {
                errorMessage = "Gagal update"
            } finally {
                isLoading = false
            }
        }
    }
    fun setEditData(postId: String) {
        // Cari data post di dalam postList berdasarkan ID
        val post = postList.find { it.id == postId }

        if (post != null) {
            // Isi variabel state dengan data dari post tersebut
            title = post.title
            content = post.content
            selectedCategory = post.category

            // Reset nama file agar tidak membingungkan (karena ini data lama)
            thumbnailName = "Current Thumbnail: ${post.thumbnail ?: "None"}"
            imageContentName = "Current Image: ${post.imageContent ?: "None"}"

            // Reset URI agar tidak mencoba upload ulang gambar lama kecuali user memilih yang baru
            thumbnailUri = null
            imageContentUri = null
        } else {
            // Opsional: Jika tidak ketemu di list, bisa ambil dari API detail
            fetchPostDetailFromServer(postId)
        }
    }
    // Fungsi tambahan jika postList kosong (misal user refresh halaman edit)
    private fun fetchPostDetailFromServer(postId: String) {
        viewModelScope.launch {
            try {
                val post = RetroFitClient.instance.getPostDetail(postId)
                title = post.title
                content = post.content
                selectedCategory = post.category
            } catch (e: Exception) {
                errorMessage = "Gagal memuat data postingan"
            }
        }
    }

    fun onSearch(query: String) {
        searchQuery = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            isLoading = true
            try {
                val response = RetroFitClient.instance.getPosts(title = query)
                postList = response
            } catch (e: Exception) {
                errorMessage = "Gagal melakukan pencarian"
            } finally {
                isLoading = false
            }
        }
    }
    private var searchJob: kotlinx.coroutines.Job? = null

    private suspend fun uploadFile(context: Context, uri: Uri, postId: String, type: String, token: String): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes() ?: return "File Null"
            val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
            val requestFile = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())

            val partName = "file"

            val body = MultipartBody.Part.createFormData(partName, "image_${type}.jpg", requestFile)

            val uploadResponse = if (type == "thumbnail") {
                RetroFitClient.instance.uploadThumbnail(postId, body, token)
            } else {
                RetroFitClient.instance.uploadImageContent(postId, body, token)
            }

            if (uploadResponse.isSuccessful) {
                "Sukses (200)"
            } else {
                // Tambahkan log detail untuk debugging jika gagal
                val errorLog = uploadResponse.errorBody()?.string()
                Log.e("DEBUG_UPLOAD", "Gagal $type: $errorLog")
                "Gagal (${uploadResponse.code()})"
            }
        } catch (e: Exception) {
            "Error Exception: ${e.message}"
        }
    }
}
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.viewModel.UserViewModel

@Composable
fun UserDetailScreen(userId: String, navController: NavController, viewModel: UserViewModel = viewModel()) {
    LaunchedEffect(userId) {
        viewModel.fetchUserDetail(userId)
    }

    if (viewModel.isLoading && viewModel.selectedUser == null) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
    } else {
        viewModel.selectedUser?.let { user ->
            ProfileLayout(
                firstName = user.firstName,
                lastName = user.lastName,
                photo = user.photo,
                joinDate = user.joinDate,
                dateOfBirth = user.dateOfBirth,
                postList = viewModel.userPosts,
                isOwnProfile = false, // Tombol Update & Add Post tidak muncul
                isLoading = viewModel.isLoading,
                selectedTabIndex = 0,
                onTabSelected = {},
                navController = navController
            )
        }
    }
}
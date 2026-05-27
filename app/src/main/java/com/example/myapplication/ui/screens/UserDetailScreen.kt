package com.example.myapplication.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.viewModel.UserViewModel

@Composable
fun UserDetailScreen(userId: String, navController: NavController, viewModel: UserViewModel = viewModel()) {
    LaunchedEffect(userId) {
        viewModel.fetchUserDetail(userId)
    }

    viewModel.selectedUser?.let { user ->
        ProfileLayout(
            user = user,
            postList = viewModel.selectedUserPosts,
            isOwnProfile = false,
            isLoading = viewModel.isLoading,
            selectedTabIndex = 0,
            onTabSelected = {},
            navController = navController
        )
    }
}
package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.ui.components.FollowButton
import com.example.myapplication.ui.viewModel.UserViewModel
import com.example.myapplication.util.GlobalData

@Composable
fun UserDetailScreen(
        userId: String,
        navController: NavController,
        viewModel: UserViewModel = viewModel()
    ) {

    LaunchedEffect(userId) {
        viewModel.fetchUserDetail(userId)
    }

    val isFollowing by viewModel.isFollowing.collectAsState()
    val currentUser = GlobalData.myUserId

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

    if (profileUser.id != currentUser?.id) {
        FollowButton(
            isFollowing = isFollowing,
            onClick = { viewModel.toggleFollow(profileUser.id) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
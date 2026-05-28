package com.example.myapplication.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext

@Composable
fun UserDetailScreen(
        userId: String,
        navController: NavController,
        viewModel: UserViewModel = viewModel()
    ) {

    val isFollowing = viewModel.isFollowing
    val myId = GlobalData.myUserId
    val context = LocalContext.current

    LaunchedEffect(userId) {
        viewModel.fetchUserDetail(userId)
    }

    LaunchedEffect(viewModel.errorMessage) {
        if (viewModel.errorMessage.isNotEmpty()) {
            Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.errorMessage = "" // Reset error setelah tampil
        }
    }

    if (viewModel.isLoading && viewModel.selectedUser == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        viewModel.selectedUser?.let { user ->
            Box(modifier = Modifier.fillMaxSize()) {
                ProfileLayout(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    photo = user.photo,
                    joinDate = user.joinDate,
                    dateOfBirth = user.dateOfBirth,
                    postList = viewModel.userPosts,
                    isOwnProfile = user.id == myId,
                    isLoading = viewModel.isLoading,
                    selectedTabIndex = 0,
                    onTabSelected = {},
                    navController = navController,
                    followersCount = user.followersCount,
                    followingCount = user.followingCount,
                    actionButton = {
                        if (user.id != myId) {
                            FollowButton(
                                isFollowing = isFollowing,
                                onClick = { viewModel.toggleFollow(user.id ?: "") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    userId = userId
                )
            }
        }
    }
}